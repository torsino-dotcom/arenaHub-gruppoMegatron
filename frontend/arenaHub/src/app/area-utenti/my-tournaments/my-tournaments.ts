import { Component, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../auth/auth.service';
import { TournamentEnrollmentService } from '../../features/tornei/tournament-enrollment.service';
import { MOCK_TOURNAMENTS } from '../../features/tornei/mocktournaments';
import { RouterLink } from '@angular/router';

@Component({
  imports: [CommonModule, RouterLink],
  standalone: true,
  selector: 'app-my-tournaments',
  styleUrl: './my-tournaments.scss',
  templateUrl: './my-tournaments.html',
})
export class MyTournaments {
  readonly authService = inject(AuthService)
  private readonly enrollmentService = inject(TournamentEnrollmentService)
  
  readonly myTournaments = computed(() => {
    const user = this.authService.currentUser()
    if (!user) return []

    return this.enrollmentService.registrationsFor(user.email).flatMap(registration =>  {
      const tournament = MOCK_TOURNAMENTS.find(item =>
        item.id_tornei === registration.tournamentId
      )

      if(!tournament) return []

      return [{
        tournament,
        status: registration.status,
        occupiedPlaces: tournament.posti_occupati + this.enrollmentService.confirmedCount(tournament.id_tornei)
      }]
    })
  })

  cancel(tournamentId: number): void {
    const user = this.authService.currentUser()

    if(!user) return

    this.enrollmentService.cancel(tournamentId, user.email)
  }
}
