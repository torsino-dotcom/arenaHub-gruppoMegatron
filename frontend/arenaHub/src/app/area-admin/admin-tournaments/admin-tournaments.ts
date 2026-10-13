import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { TournamentDetail, TournamentStatus } from '../../features/tornei/tornei.model';


@Component({
  imports: [CommonModule, RouterLink],
  standalone: true,
  selector: 'app-admin-tournaments',
  styleUrl: './admin-tournaments.scss',
  templateUrl: './admin-tournaments.html',
})
export class AdminTournaments {
  private readonly router = inject(Router);
  tournaments = signal<TournamentDetail[]>([
    {
      id_tornei: 1,
      nome_torneo: "Torneo d'autunno",
      descrizione: 'Torneo ufficiale di League of Legends',
      gioco: 'League of Legends',
      data_ora: '16/10 15:00',
      data_inizio_iscrizioni: '01/10/2026',
      data_fine_iscrizioni: '15/10/2026',
      posti_max: 16,
      posti_occupati: 3,
      stato: 'aperto'
    },
    {
      id_tornei: 2,
      nome_torneo: 'Coppa ITS',
      descrizione: 'Competizione a squadre di Valorant',
      gioco: 'Valorant',
      data_ora: '18/10 14:00',
      data_inizio_iscrizioni: '02/10/2026',
      data_fine_iscrizioni: '17/10/2026',
      posti_max: 16,
      posti_occupati: 16,
      stato: 'chiuso'
    }
  ]);

  selectedTournamentForMembers = signal<TournamentDetail | null>(null);

  onCreateTournament(): void {
    console.log('Apertura form/modal Creazione Torneo (POST /api/tornei)');
  }

  onEditTournament(tournament: TournamentDetail): void {
    console.log('Modifica torneo (PUT /api/tornei/' + tournament.id_tornei + ')');
  }

  toggleStatus(tournament: TournamentDetail): void {
    const newStatus: TournamentStatus = tournament.stato === 'aperto' ? 'chiuso' : 'aperto';

    this.tournaments.update(list =>
      list.map(t => t.id_tornei === tournament.id_tornei ? { ...t, stato: newStatus } : t)
    );

    console.log(`Aggiornato stato torneo ${tournament.id_tornei} a ${newStatus} (PUT /api/tornei/${tournament.id_tornei})`);
  }

  onViewRegistered(tournament: TournamentDetail): void {
    this.selectedTournamentForMembers.set(tournament);
    console.log(`Richiesta iscritti per il torneo ${tournament.id_tornei} (GET iscritti)`);
  }

  closeMembersModal(): void {
    this.selectedTournamentForMembers.set(null);
  }
}
