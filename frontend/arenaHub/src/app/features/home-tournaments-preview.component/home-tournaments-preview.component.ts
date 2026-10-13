import { Component, signal } from '@angular/core';
import { TournamentListItem } from '../tornei/tornei.model';
import { RouterLink } from '@angular/router';
import { TournamentCardComponent } from '../tornei/tournament-card.component/tournament-card.component';

@Component({
  imports: [RouterLink, TournamentCardComponent],
  selector: 'app-home-tournaments-preview',
  styleUrl: './home-tournaments-preview.component.scss',
  templateUrl: './home-tournaments-preview.component.html',
})
export class HomeTournamentsPreviewComponent {
  readonly tournaments = signal<TournamentListItem[]>([
    {
      id_tornei: 1,
      nome_torneo: 'Arena Clash',
      descrizione: 'Una sfida aperta a tutti gli appassionati.',
      gioco: 'League of Legends',
      data_ora: '2026-11-15T18:00:00',
      data_inizio_iscrizioni: '2026-10-20T09:00:00',
      data_fine_iscrizioni: '2026-11-12T23:59:00',
      posti_max: 32,
      stato: 'aperto',
    },
    {
      id_tornei: 2,
      nome_torneo: 'Valorant Night',
      descrizione: 'Forma la tua squadra e scendi in campo.',
      gioco: 'Valorant',
      data_ora: '2026-11-20T20:00:00',
      data_inizio_iscrizioni: '2026-10-25T09:00:00',
      data_fine_iscrizioni: '2026-11-17T23:59:00',
      posti_max: 16,
      stato: 'aperto',
    },
    {
      id_tornei: 3,
      nome_torneo: 'Rocket League Cup',
      descrizione: 'Una competizione veloce, tutta da seguire.',
      gioco: 'Rocket League',
      data_ora: '2026-11-28T17:00:00',
      data_inizio_iscrizioni: '2026-10-28T09:00:00',
      data_fine_iscrizioni: '2026-11-25T23:59:00',
      posti_max: 24,
      stato: 'aperto',
    },
  ])
}
