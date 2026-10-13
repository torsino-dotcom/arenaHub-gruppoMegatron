import { Component, computed, signal } from '@angular/core';
import { TournamentListItem, TournamentStatus } from '../tornei.model';
import { TournamentCardComponent } from '../tournament-card.component/tournament-card.component';

type StatusFilter = TournamentStatus | 'tutti'

@Component({
  imports: [TournamentCardComponent],
  selector: 'app-tournament-list-page',
  styleUrl: './tournament-list-page.component.scss',
  templateUrl: './tournament-list-page.component.html',
})
export class TournamentListPageComponent {
  readonly searchTerm = signal('')
  readonly selectedGame = signal('tutti')
  readonly selectedStatus = signal<StatusFilter>('tutti')

  //? Dati di prova:
  readonly tournaments = signal<TournamentListItem[]>([
    {
      id_tornei: 1,
      nome_torneo: 'ArenaHub Open',
      descrizione: 'Torneo aperto a tutti i giocatori.',
      gioco: 'League of Legends',
      data_ora: '2026-10-16T15:00:00',
      data_inizio_iscrizioni: '2026-10-01',
      data_fine_iscrizioni: '2026-10-15',
      posti_max: 16,
      stato: 'aperto',
    },
    {
      id_tornei: 2,
      nome_torneo: 'Valorant Friday',
      descrizione: 'Sfida settimanale in formato eliminazione diretta.',
      gioco: 'Valorant',
      data_ora: '2026-10-20T18:00:00',
      data_inizio_iscrizioni: '2026-10-05',
      data_fine_iscrizioni: '2026-10-19',
      posti_max: 8,
      stato: 'aperto',
    },
    {
      id_tornei: 3,
      nome_torneo: 'Coppa ArenaHub',
      descrizione: 'Le iscrizioni sono terminate.',
      gioco: 'League of Legends',
      data_ora: '2026-10-22T16:00:00',
      data_inizio_iscrizioni: '2026-10-01',
      data_fine_iscrizioni: '2026-10-18',
      posti_max: 16,
      stato: 'chiuso',
    },
  ])

  //giochi ricavati dai tornei
  readonly games = computed(() => 
    [...new Set(this.tournaments().map(tournament => tournament.gioco))].sort()
  )

  //query filtri
  readonly filteredTournaments = computed(() => {
    const query = this.searchTerm().trim().toLocaleLowerCase()
    const game = this.selectedGame()
    const status = this.selectedStatus()

    return this.tournaments().filter(tournament => {
      //nome o descrizione
      const matchesQuery =
        query.length === 0 ||
        tournament.nome_torneo.toLocaleLowerCase().includes(query) ||
        tournament.descrizione.toLocaleLowerCase().includes(query)
      
        const matchesGame =
          game === 'tutti' || tournament.gioco === game
        
        const matchesStatus = 
          status === 'tutti' || tournament.stato === status

      return matchesQuery && matchesGame && matchesStatus
    })
  })

  setSearchTerm(event: Event): void {
    const input = event.target as HTMLInputElement
    this.searchTerm.set(input.value)
  }

  setGame(event: Event): void {
    const select = event.target as HTMLSelectElement
    this.selectedGame.set(select.value)
  }

  setStatus(event: Event): void {
    const select = event.target as HTMLSelectElement
    this.selectedStatus.set(select.value as StatusFilter)
  }

  resetFilters(): void {
    this.searchTerm.set('')
    this.selectedGame.set('tutti')
    this.selectedStatus.set('tutti')
  }

  statusLabel(status: TournamentStatus): string {
    const label = status.replace('_', ' ')
    return label.charAt(0).toLocaleLowerCase() + label.slice(1)
  }
}
