import { Component, computed, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TournamentListItem, TournamentStatus } from '../tornei.model';
import { TournamentCardComponent } from '../tournament-card.component/tournament-card.component';
import { MOCK_TOURNAMENTS } from '../mocktournaments';
import { TournamentStoreService } from '../tournament-store.service';

type StatusFilter = TournamentStatus | 'tutti'

@Component({
  imports: [TournamentCardComponent],
  selector: 'app-tournament-list-page',
  styleUrl: './tournament-list-page.component.scss',
  templateUrl: './tournament-list-page.component.html',
})
export class TournamentListPageComponent implements OnInit {
  readonly searchTerm = signal('')
  readonly selectedGame = signal('tutti')
  readonly selectedStatus = signal<StatusFilter>('tutti')


  private readonly route = inject(ActivatedRoute);
  private readonly tournamentStore = inject(TournamentStoreService)
  //! prende dati mock
  readonly tournaments = this.tournamentStore.tournaments

  //giochi ricavati dai tornei
  readonly games = computed(() =>
    [...new Set(this.tournaments().map(tournament => tournament.gioco))].sort()
  );

  ngOnInit(): void {
    
      this.route.queryParams.subscribe(params => {
        if (params['game']) {
          this.selectedGame.set(params['game']);
        }
      });
   }

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
