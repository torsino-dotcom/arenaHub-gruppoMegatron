import { Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../auth/auth.service';
import { TournamentEnrollmentService } from '../../features/tornei/tournament-enrollment.service';
import { MOCK_TOURNAMENTS } from '../../features/tornei/mocktournaments';
import { RouterLink } from '@angular/router';
import { TournamentEnrollmentApiService } from '../../features/tornei/services/tournament-enrollment-api.service';
import { GiochiApiService } from '../../features/giochi/giochi-api.service';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { catchError, combineLatest, finalize, of, switchMap } from 'rxjs';
import { EnrollmentApiStatus, MyTournamentEnrollmentApi } from '../../features/tornei/tornei.model';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  imports: [CommonModule, RouterLink],
  standalone: true,
  selector: 'app-my-tournaments',
  styleUrl: './my-tournaments.scss',
  templateUrl: './my-tournaments.html',
})
export class MyTournaments {
  readonly authService = inject(AuthService)
  // private readonly enrollmentService = inject(TournamentEnrollmentService)

  private readonly enrollmentApi = inject(TournamentEnrollmentApiService)
  private readonly giochiApi = inject(GiochiApiService)

  readonly loading = signal(true)
  readonly errorMessage = signal<string | null>(null)
  readonly successMessage = signal<string | null>(null)
  readonly cancelingTournamentId = signal<number | null>(null)

  private readonly refresh = signal(0)

  private readonly games = toSignal(
    this.giochiApi.getAll().pipe(
      catchError(() => of([]))
    ),
    { initialValue: [] }
  )

  private readonly enrollments = toSignal(
    combineLatest({
      user: toObservable(this.authService.currentUser),
      refresh: toObservable(this.refresh)
    }).pipe(
      switchMap(({ user }) => {
        if (!user) {
          this.loading.set(false)
          return of([] as MyTournamentEnrollmentApi[])
        }

        this.loading.set(true)
        this.errorMessage.set(null)

        return this.enrollmentApi.getMine().pipe(
          catchError((error: unknown) => {
            this.errorMessage.set(this.apiErrorMessage(error))
            return of([] as MyTournamentEnrollmentApi[])
          }),
          finalize(() => this.loading.set(false))
        )
      })
    ),
    { initialValue: [] as MyTournamentEnrollmentApi[] }
  )

  readonly myTournaments = computed(() =>
    this.enrollments()
      .filter(item => item.stato !== 'annullato')
      .map(item => ({
        ...item,
        gioco: this.games().find(game => game.id_giochi === item.id_giochi)?.nome_giochi
          ?? 'Gioco non disponibile'
      }))
      .sort((first, second) =>
        new Date(first.data_ora).getTime() - new Date(second.data_ora).getTime()
      )
  )

  statusLabel(status: EnrollmentApiStatus): string {
    switch (status) {
      case 'confermato':
        return 'Iscritto'
      case 'in_coda':
        return 'Lista d’attesa'
      default:
        return 'Annullato'
    }
  }

  cancel(tournamentId: number): void {
    if (!this.authService.currentUser() || this.cancelingTournamentId() !== null) {
      return
    }

    this.errorMessage.set(null)
    this.successMessage.set(null)
    this.cancelingTournamentId.set(tournamentId)

    this.enrollmentApi.cancelMine(tournamentId).pipe(
      finalize(() => this.cancelingTournamentId.set(null))
    ).subscribe({
      next: () => {
        this.successMessage.set('Iscrizione annullata.')
        this.refresh.update(value => value + 1)
      },
      error: (error: unknown) => {
        this.errorMessage.set(this.apiErrorMessage(error))
      }
    })
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
