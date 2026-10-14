import { DatePipe } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { TournamentDetail, TournamentStatus } from '../tornei.model';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AuthService } from '../../../auth/auth.service';
import { TournamentEnrollmentService } from '../tournament-enrollment.service';
import { toSignal } from '@angular/core/rxjs-interop';
import { TournamentMatchesComponent } from '../tournament-matches.component/tournament-matches.component';
import { TournamentStoreService } from '../tournament-store.service';

@Component({
  imports: [DatePipe, RouterLink, TournamentMatchesComponent],
  selector: 'app-tournament-detail-page',
  standalone: true,
  styleUrl: './tournament-detail-page.component.scss',
  templateUrl: './tournament-detail-page.component.html',
})
export class TournamentDetailPageComponent {
  private readonly route = inject(ActivatedRoute)
  private readonly tournamentStore = inject(TournamentStoreService)

  private readonly routeParams = toSignal(this.route.paramMap, {
    initialValue: this.route.snapshot.paramMap
  })

  readonly authService = inject(AuthService)
  private readonly enrollmentService = inject(TournamentEnrollmentService)

  readonly isAdmin = computed(
    () => this.authService.currentUser()?.ruolo === 'admin'
  )

  readonly tournament = computed(() => {
    const id = Number(this.routeParams().get('id'))

    return this.tournamentStore.findById(id)
  })

  readonly currentRegistration = computed(() => {
    const user = this.authService.currentUser()
    const tournament = this.tournament()

    if (!user || !tournament) return null

    return this.enrollmentService.findActive(tournament.id_tornei, user.email)
  })

  readonly occupiedPlaces = computed(() => {
    const tournament = this.tournament()

    if (!tournament) return 0

    return tournament.posti_occupati +
      this.enrollmentService.confirmedCount(tournament.id_tornei)
  })



  readonly isFull = computed(() => {
    const tournament = this.tournament()

    return !tournament || this.occupiedPlaces() >= tournament.posti_max
  })

  readonly occupancyPercent = computed(() => {
    const tournament = this.tournament()

    if (!tournament || tournament.posti_max <= 0) return 0

    return Math.min(100, Math.round((this.occupiedPlaces() / tournament.posti_max) * 100))
  })

  readonly registrationIsOpen = computed(() => {
    const tournament = this.tournament()
    const today = this.localDateString()

    if (!tournament) return false

    return (
      tournament.stato === 'aperto' &&
      today >= tournament.data_inizio_iscrizioni &&
      today <= tournament.data_fine_iscrizioni
    )
  })

  enroll(): void {
    const user = this.authService.currentUser()
    const tournament = this.tournament()

    if (!user || !tournament || !this.registrationIsOpen()) {
      return
    }

    this.enrollmentService.enroll(tournament, user)
  }

  statusLabel(status: TournamentStatus): string {
    const label = status.replace('_', ' ')
    return label.charAt(0).toLocaleLowerCase() + label.slice(1)
  }

  private localDateString(): string {
    const date = new Date()
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')

    return `${year}-${month}-${day}`
  }
}
