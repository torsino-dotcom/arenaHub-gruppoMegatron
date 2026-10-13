import { DatePipe } from '@angular/common';
import { Component, computed, signal } from '@angular/core';
import { TouranmentDetail, TournamentStatus } from '../tornei.model';
import { RouterLink } from '@angular/router';

@Component({
  imports: [DatePipe, RouterLink],
  selector: 'app-tournament-detail-page',
  standalone: true,
  styleUrl: './tournament-detail-page.component.scss',
  templateUrl: './tournament-detail-page.component.html',
})
export class TournamentDetailPageComponent {
  //? dati di prova
  readonly tournament = signal<TouranmentDetail>({
    id_tornei: 1,
    nome_torneo: 'ArenaHub Open',
    descrizione:
      'Un torneo aperto alla community. Forma la tua squadra e preparati a scendere in arena.',
    gioco: 'League of Legends',
    data_ora: '2026-10-20T18:00:00',
    data_inizio_iscrizioni: '2026-10-01',
    data_fine_iscrizioni: '2026-10-19',
    posti_max: 16,
    posti_occupati: 9,
    stato: 'aperto',
  })

  readonly isFull = computed(
    () => this.tournament().posti_occupati >= this.tournament().posti_max
  )

  readonly occupancyPercent = computed(() => {
    const tournament = this.tournament()
    
    return Math.min(100, Math.round((tournament.posti_occupati / tournament.posti_max) * 100))
  })

  readonly registrationIsOpen = computed(() => {
    const tournament = this.tournament()
    const today = this.localDateString()
    
    return (
      tournament.stato === 'aperto' &&
      today >= tournament.data_inizio_iscrizioni &&
      today <= tournament.data_fine_iscrizioni
    )
  })

  statusLabel(status: TournamentStatus): string {
    const label = status.replace('_', ' ')
    return label.charAt(0).toLocaleLowerCase() + label.slice(1)
  }

  private localDateString(): string {
    const date = new Date()
    const year = date.getFullYear()
    const month = String(date.getMonth() +1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')

    return `${year}-${month}-${day}`
  }
}
