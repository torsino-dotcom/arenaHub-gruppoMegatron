import { Component, computed, inject, signal } from '@angular/core';
import { TournamentDetail, TournamentStatus } from '../../features/tornei/tornei.model';
import { NewTournament, TournamentStoreService } from '../../features/tornei/tournament-store.service';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';

@Component({
  imports: [RouterLink, DatePipe],
  selector: 'app-admin-tournaments-page',
  styleUrl: './admin-tournaments-page.component.scss',
  templateUrl: './admin-tournaments-page.component.html',
})
export class AdminTournamentsPageComponent {
  private readonly tournamentStore = inject(TournamentStoreService)

  readonly formOpen = signal(false)
  readonly editingTournamentId = signal<number | null>(null)
  readonly errorMessage = signal<string | null>(null)

  readonly tournaments = computed(() =>
    [...this.tournamentStore.tournaments()].sort((first, second) =>
      new Date(first.data_ora).getTime() -
      new Date(second.data_ora).getTime(),
    ),
  )

  readonly form = signal<NewTournament>(this.emptyForm())

  setText(
    field:
      | 'nome_torneo'
      | 'descrizione'
      | 'gioco'
      | 'data_ora'
      | 'data_inizio_iscrizioni'
      | 'data_fine_iscrizioni',
    event: Event,
  ): void {
    const value = (event.target as HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement).value

    this.form.update(current => ({
      ...current,
      [field]: value,
    }))
  }

  setPostiMax(event: Event): void {
    const value = Number((event.target as HTMLInputElement).value)

    this.form.update(current => ({
      ...current,
      posti_max: value,
    }))
  }

  setStatus(event: Event): void {
    const value = (event.target as HTMLSelectElement).value as TournamentStatus

    this.form.update(current => ({
      ...current,
      stato: value,
    }))
  }

  editTournament(tournament: TournamentDetail): void {
    this.errorMessage.set(null)
    this.editingTournamentId.set(tournament.id_tornei)
    this.form.set({
      nome_torneo: tournament.nome_torneo,
      descrizione: tournament.descrizione,
      gioco: tournament.gioco,
      // datetime-local vuole il valore senza i secondi
      data_ora: tournament.data_ora.slice(0, 16),
      data_inizio_iscrizioni: tournament.data_inizio_iscrizioni,
      data_fine_iscrizioni: tournament.data_fine_iscrizioni,
      posti_max: tournament.posti_max,
      stato: tournament.stato,
    })

    this.formOpen.set(true)
  }

  saveTournament(event: Event): void {
    event.preventDefault()
    this.errorMessage.set(null)

    const data = this.form()

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

    const tournamentData: NewTournament = {
      ...data,
      data_ora: data.data_ora.length === 16
        ? `${data.data_ora}:00`
        : data.data_ora,
    }

    const editingId = this.editingTournamentId()

    if (editingId !== null) {
      try {
        const updated = this.tournamentStore.update(editingId, tournamentData)

        if (!updated) {
          this.errorMessage.set('Il torneo non è stato trovato.')
          return
        }
      } catch (error) {
        this.errorMessage.set(
          error instanceof Error ? error.message : 'Impossibile aggiornare il torneo.'
        )
        return
      }
    } else {
      this.tournamentStore.create(tournamentData)
    }

    this.closeForm()
  }

  deleteTournament(id: number, name: string): void {
    const confirmed = window.confirm(`Vuoi eliminare il torneo "${name}"?`)

    if (!confirmed) return

    this.tournamentStore.delete(id)

    if(this.editingTournamentId() === id) {
      this.closeForm()
    }
  }

  toggleForm(): void {
    if(this.formOpen()) {
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

  private emptyForm(): NewTournament {
    return {
      nome_torneo: '',
      descrizione: '',
      gioco: 'League of Legends',
      data_ora: '',
      data_inizio_iscrizioni: '',
      data_fine_iscrizioni: '',
      posti_max: 16,
      stato: 'bozza',
    }
  }
}
