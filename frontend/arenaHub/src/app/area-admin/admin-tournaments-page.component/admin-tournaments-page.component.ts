import { Component, computed, inject, signal } from '@angular/core';
import { TournamentStatus } from '../../features/tornei/tornei.model';
import { NewTournament, TournamentStoreService } from '../../features/tornei/tournament-store.service';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';

@Component({
  imports: [RouterLink, DatePipe],
  selector: 'app-admin-tournaments-page.component',
  styleUrl: './admin-tournaments-page.component.scss',
  templateUrl: './admin-tournaments-page.component.html',
})
export class AdminTournamentsPageComponent {
  private readonly tournamentStore = inject(TournamentStoreService)

  readonly formOpen = signal(false)
  readonly errorMessage = signal<string | null>(null)

  readonly tournaments = computed(() =>
    [...this.tournamentStore.tournaments()].sort((first, second) =>
      new Date(first.data_ora).getTime() -
      new Date(second.data_ora).getTime(),
    ),
  )

  readonly form = signal<NewTournament>({
    nome_torneo: '',
    descrizione: '',
    gioco: 'League of Legends',
    data_ora: '',
    data_inizio_iscrizioni: '',
    data_fine_iscrizioni: '',
    posti_max: 16,
    stato: 'bozza',
  })

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
    const value = (event.target as HTMLInputElement).value

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

  createTournament(event: Event): void {
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

    const dataOra = data.data_ora.length === 16
      ? `${data.data_ora}:00`
      : data.data_ora

    this.tournamentStore.create({
      ...data,
      data_ora: dataOra,
    })

    this.form.set({
      nome_torneo: '',
      descrizione: '',
      gioco: 'League of Legends',
      data_ora: '',
      data_inizio_iscrizioni: '',
      data_fine_iscrizioni: '',
      posti_max: 16,
      stato: 'bozza',
    })

    this.formOpen.set(false)
  }

  deleteTournament(id: number, name: string): void {
    const confirmed = window.confirm(`Vuoi eliminare il torneo "${name}"?`)

    if (confirmed) {
      this.tournamentStore.delete(id)
    }
  }

  toggleForm(): void {
    this.errorMessage.set(null)
    this.formOpen.update(open => !open)
  }

  statusLabel(status: TournamentStatus): string {
    return status.replace('_', ' ')
  }
}
