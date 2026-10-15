import { Component, computed, inject, signal } from '@angular/core';
import { NewTournamentApi, TournamentApiItem, TournamentDetail, TournamentStatus } from '../../features/tornei/tornei.model';
import { NewTournament, TournamentStoreService } from '../../features/tornei/tournament-store.service';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { TorneiApiService } from '../../features/tornei/services/tornei-api.service';
import { GiochiApiService } from '../../features/giochi/giochi-api.service';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { catchError, finalize, of, switchMap } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

type AdminTournamentRow = TournamentApiItem & {
  gioco: string
}

@Component({
  imports: [RouterLink, DatePipe],
  selector: 'app-admin-tournaments-page',
  styleUrl: './admin-tournaments-page.component.scss',
  templateUrl: './admin-tournaments-page.component.html',
})
export class AdminTournamentsPageComponent {
  // private readonly tournamentStore = inject(TournamentStoreService)
  private readonly torneiApi = inject(TorneiApiService)
  private readonly giochiApi = inject(GiochiApiService)

  readonly formOpen = signal(false)
  readonly editingTournamentId = signal<number | null>(null)
  readonly errorMessage = signal<string | null>(null)
  readonly gamesError = signal<string | null>(null)
  readonly loading = signal(true)
  readonly saving = signal(false)
  readonly deletingTournamentId = signal<number | null>(null)

  private readonly refresh = signal(0)

  readonly games = toSignal(
    this.giochiApi.getAll().pipe(
      catchError(() => {
        this.gamesError.set('Non è stato possibile caricare i giochi.')
        return of([])
      })
    ),
    { initialValue: [] }
  )

  private readonly apiTournaments = toSignal(
    toObservable(this.refresh).pipe(
      switchMap(() => {
        this.loading.set(true)
        this.errorMessage.set(null)

        return this.torneiApi.getAll().pipe(
          catchError((error: unknown) => {
            this.errorMessage.set(this.apiErrorMessage(error))
            return of([] as TournamentApiItem[])
          }),
          finalize(() => this.loading.set(false))
        )
      })
    ),
    { initialValue: [] as TournamentApiItem[] }
  )

  readonly tournaments = computed<AdminTournamentRow[]>(() =>
    this.apiTournaments()
      .map(tournament => ({
        ...tournament,
        gioco: this.games().find(
          game => game.id_giochi === tournament.id_giochi
        )?.nome_giochi ?? 'Gioco non disponibile'
      }))
      .sort((first, second) =>
        new Date(first.data_ora).getTime() -
        new Date(second.data_ora).getTime()
      )
  )

  readonly form = signal<NewTournamentApi>(this.emptyForm())

  setText(
    field:
      | 'nome_torneo'
      | 'descrizione'
      | 'data_ora'
      | 'data_inizio_iscrizioni'
      | 'data_fine_iscrizioni',
    event: Event
  ): void {
    const value = (
      event.target as HTMLInputElement | HTMLTextAreaElement
    ).value

    this.form.update(current => ({
      ...current,
      [field]: value
    }))
  }

  setGame(event: Event): void {
    const id = Number((event.target as HTMLSelectElement).value)

    this.form.update(current => ({
      ...current,
      id_giochi: id
    }))
  }

  setPostiMax(event: Event): void {
    const value = Number((event.target as HTMLInputElement).value)

    this.form.update(current => ({
      ...current,
      posti_max: value
    }))
  }

  setStatus(event: Event): void {
    const value = (event.target as HTMLSelectElement).value as TournamentStatus

    this.form.update(current => ({
      ...current,
      stato: value
    }))
  }

  editTournament(tournament: AdminTournamentRow): void {
    this.errorMessage.set(null)
    this.editingTournamentId.set(tournament.id_tornei)

    this.form.set({
      nome_torneo: tournament.nome_torneo,
      descrizione: tournament.descrizione,
      id_giochi: tournament.id_giochi,
      data_ora: tournament.data_ora.slice(0, 16),
      data_inizio_iscrizioni: tournament.data_inizio_iscrizioni.slice(0, 10),
      data_fine_iscrizioni: tournament.data_fine_iscrizioni.slice(0, 10),
      posti_max: tournament.posti_max,
      stato: tournament.stato
    })

    this.formOpen.set(true)
  }

  saveTournament(event: Event): void {
    event.preventDefault()
    this.errorMessage.set(null)

    const data = this.form()

    if (data.id_giochi <= 0) {
      this.errorMessage.set('Seleziona un gioco.')
      return
    }

    if (data.posti_max <= 0) {
      this.errorMessage.set('Il numero di posti deve essere maggiore di zero.')
      return
    }

    if (data.data_inizio_iscrizioni > data.data_fine_iscrizioni) {
      this.errorMessage.set('L’inizio delle iscrizioni deve precedere la chiusura.')
      return
    }

    if (data.data_fine_iscrizioni > data.data_ora.slice(0, 10)) {
      this.errorMessage.set('La chiusura delle iscrizioni deve precedere il torneo.')
      return
    }

    const payload: NewTournamentApi = {
      ...data,
      data_ora: data.data_ora.length === 16
        ? `${data.data_ora}:00`
        : data.data_ora
    }

    const editingId = this.editingTournamentId()
    const request = editingId !== null
      ? this.torneiApi.update(editingId, payload)
      : this.torneiApi.create(payload)

    this.saving.set(true)

    request.pipe(
      finalize(() => this.saving.set(false))
    ).subscribe({
      next: () => {
        this.closeForm()
        this.refresh.update(value => value + 1)
      },
      error: (error: unknown) => {
        this.errorMessage.set(this.apiErrorMessage(error))
      }
    })
  }

  deleteTournament(id: number, name: string): void {
    if (this.deletingTournamentId() !== null) return

    const confirmed = window.confirm(`Vuoi eliminare il torneo "${name}"?`)
    if (!confirmed) return

    this.errorMessage.set(null)
    this.deletingTournamentId.set(id)

    this.torneiApi.delete(id).pipe(
      finalize(() => this.deletingTournamentId.set(null))
    ).subscribe({
      next: () => {
        if (this.editingTournamentId() === id) {
          this.closeForm()
        }

        this.refresh.update(value => value + 1)
      },
      error: (error: unknown) => {
        this.errorMessage.set(this.apiErrorMessage(error))
      }
    })
  }

  toggleForm(): void {
    if (this.formOpen()) {
      this.closeForm()
      return
    }

    this.errorMessage.set(null)
    this.editingTournamentId.set(null)
    this.form.set(this.emptyForm())
    this.formOpen.set(true)
  }

  statusLabel(status: TournamentStatus): string {
    return status.replace('_', ' ')
  }

  private closeForm(): void {
    this.formOpen.set(false)
    this.editingTournamentId.set(null)
    this.errorMessage.set(null)
    this.form.set(this.emptyForm())
  }

  private emptyForm(): NewTournamentApi {
    return {
      nome_torneo: '',
      descrizione: '',
      id_giochi: 0,
      data_ora: '',
      data_inizio_iscrizioni: '',
      data_fine_iscrizioni: '',
      posti_max: 16,
      stato: 'bozza'
    }
  }

  private apiErrorMessage(error: unknown): string {
    if (error instanceof HttpErrorResponse) {
      const body = error.error as
        | { errore?: string; messaggio?: string }
        | string
        | null

      if (typeof body === 'string') return body

      return body?.errore
        ?? body?.messaggio
        ?? 'Non è stato possibile completare la richiesta.'
    }

    return 'Non è stato possibile completare la richiesta.'
  }
}

