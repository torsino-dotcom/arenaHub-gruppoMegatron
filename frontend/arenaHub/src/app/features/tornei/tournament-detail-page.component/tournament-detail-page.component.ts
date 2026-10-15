import { DatePipe } from '@angular/common';
import { Component, computed, inject, Signal, signal } from '@angular/core';
import { MyTournamentEnrollmentApi, TournamentDetail, TournamentStatus } from '../tornei.model';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AuthService } from '../../../auth/auth.service';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { TournamentMatchesComponent } from '../tournament-matches.component/tournament-matches.component';
import { TournamentStoreService } from '../tournament-store.service';
import { TorneiApiService } from '../services/tornei-api.service';
import { GiochiApiService } from '../../giochi/giochi-api.service';
import { catchError, combineLatest, distinctUntilChanged, finalize, map, of, switchMap } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { TournamentEnrollmentApiService } from '../services/tournament-enrollment-api.service';
import { PlatformSettingsApi } from '../../../area-admin/impostazioni.model';
import { ImpostazioniApiService } from '../../../area-admin/services/impostazioni-api.service';

@Component({
  imports: [DatePipe, RouterLink, TournamentMatchesComponent],
  selector: 'app-tournament-detail-page',
  standalone: true,
  styleUrl: './tournament-detail-page.component.scss',
  templateUrl: './tournament-detail-page.component.html',
})
export class TournamentDetailPageComponent {
  private readonly settingsApi = inject(ImpostazioniApiService)
  private readonly torneiApi = inject(TorneiApiService)
  private readonly giochiApi = inject(GiochiApiService)
  private readonly route = inject(ActivatedRoute)
  private readonly tournamentStore = inject(TournamentStoreService)
  readonly authService = inject(AuthService)
  private readonly enrollmentApi = inject(TournamentEnrollmentApiService)

  readonly tournamentLoading = signal(true)
  readonly tournamentError = signal<string | null>(null)

  readonly enrollmentLoading = signal(false)
  readonly enrollmentError = signal<string | null>(null)
  readonly enrollmentMessage = signal<string | null>(null)
  private readonly enrollmentRefresh = signal(0)

  readonly settingsLoading = signal(true)
  readonly settingsError = signal<string | null>(null)
  readonly myEnrollmentsLoading = signal(true)
  readonly myEnrollmentsError = signal<string | null>(null)

  private readonly routeParams = toSignal(this.route.paramMap, {
    initialValue: this.route.snapshot.paramMap
  })

  readonly isAdmin = computed(
    () => this.authService.currentUser()?.ruolo === 'admin'
  )

  private readonly platformSettings = toSignal(
    this.settingsApi.get().pipe(
      catchError(() => {
        this.settingsError.set('Non è stato possibile verificare il limite di iscrizioni.')
        return of(null as PlatformSettingsApi | null)
      }),
      finalize(() => this.settingsLoading.set(false))
    ),
    { initialValue: null as PlatformSettingsApi | null }
  )

  readonly maxActiveTournaments = computed(
    () => this.platformSettings()?.max_tornei_attivi_utente ?? null
  )

  readonly activeEnrollmentCount = computed(() =>
    this.myEnrollments().filter(enrollment =>
      enrollment.stato === 'confermato' ||
      enrollment.stato === 'in_coda'
    ).length
  )

  readonly hasReachedActiveLimit = computed(() => {
    const limit = this.maxActiveTournaments()
    return limit !== null && this.activeEnrollmentCount() >= limit
  })

  private readonly games = toSignal(
    this.giochiApi.getAll().pipe(
      catchError(() => of([]))
    ),
    { initialValue: [] }
  )

  // readonly tournament = computed(() => {
  //   const id = Number(this.routeParams().get('id'))

  //   return this.tournamentStore.findById(id)
  // })
  private readonly apiTournament = toSignal(
    this.route.paramMap.pipe(
      map(params => Number(params.get('id'))),
      distinctUntilChanged(),
      switchMap(id => {
        this.tournamentLoading.set(true)
        this.tournamentError.set(null)

        if (!Number.isInteger(id) || id <= 0) {
          this.tournamentLoading.set(false)
          return of(null)
        }

        return this.torneiApi.getById(id).pipe(
          catchError(error => {
            if (error.status !== 404) {
              this.tournamentError.set('Non è stato possibile caricare il torneo.')
            }

            return of(null)
          }),
          finalize(() => this.tournamentLoading.set(false))
        )
      })
    ),
    { initialValue: null }
  )
  readonly tournament = computed<TournamentDetail | null>(() => {
    const item = this.apiTournament()

    if (!item) return null

    const gioco = this.games().find(
      game => game.id_giochi === item.id_giochi
    )?.nome_giochi

    return {
      id_tornei: item.id_tornei,
      nome_torneo: item.nome_torneo,
      descrizione: item.descrizione,
      gioco: gioco ?? 'Gioco non disponibile',
      data_ora: item.data_ora,
      data_inizio_iscrizioni: item.data_inizio_iscrizioni,
      data_fine_iscrizioni: item.data_fine_iscrizioni,
      posti_max: item.posti_max,
      stato: item.stato,
      posti_occupati: item.posti_occupati
    }
  })

  readonly currentRegistration = computed<MyTournamentEnrollmentApi | null>(() => {
    const user = this.authService.currentUser()
    const tournament = this.tournament()

    if (!user || !tournament) return null

    return this.myEnrollments().find(
      (registration: MyTournamentEnrollmentApi) =>
        registration.id_tornei === tournament.id_tornei &&
        registration.stato !== 'annullato'
    ) ?? null
  })

  private readonly myEnrollments: Signal<MyTournamentEnrollmentApi[]> = toSignal(
    combineLatest({
      user: toObservable(this.authService.currentUser),
      refresh: toObservable(this.enrollmentRefresh)
    }).pipe(
      switchMap(({ user }) => {
        if (!user) {
          this.myEnrollmentsLoading.set(false)
          return of([] as MyTournamentEnrollmentApi[])
        }

        this.myEnrollmentsLoading.set(true)
        this.myEnrollmentsError.set(null)

        return this.enrollmentApi.getMine().pipe(
          catchError(() => {
            this.myEnrollmentsError.set('Non è stato possibile caricare le tue iscrizioni.')
            return of([] as MyTournamentEnrollmentApi[])
          }),
          finalize(() => this.myEnrollmentsLoading.set(false))
        )
      })
    ),
    { initialValue: [] as MyTournamentEnrollmentApi[] }
  )

  // readonly occupiedPlaces = computed(() => {
  //   const tournament = this.tournament()

  //   if (!tournament) return 0

  //   return tournament.posti_occupati +
  //     this.enrollmentService.confirmedCount(tournament.id_tornei)
  // })
  readonly occupiedPlaces = computed(() => this.tournament()?.posti_occupati ?? 0)



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

    if (
      !user ||
      !tournament ||
      !this.registrationIsOpen() ||
      this.enrollmentLoading() ||
      this.settingsLoading() ||
      this.myEnrollmentsLoading() ||
      this.settingsError() ||
      this.myEnrollmentsError() ||
      this.hasReachedActiveLimit()
    ) {
      return
    }

    this.enrollmentError.set(null)
    this.enrollmentMessage.set(null)
    this.enrollmentLoading.set(true)

    this.enrollmentApi.enroll(tournament.id_tornei).subscribe({
      next: () => {
        this.enrollmentMessage.set('Iscrizione inviata con successo.')
        this.enrollmentLoading.set(false)
        this.enrollmentRefresh.update(value => value + 1)
      },
      error: (error: unknown) => {
        // MODIFICA: mostra il messaggio specifico restituito dall'API, se presente
        if (error instanceof HttpErrorResponse) {
          const body = error.error as { errore?: string; messaggio?: string } | null
          this.enrollmentError.set(
            body?.errore ?? body?.messaggio ?? 'Non è stato possibile iscriversi al torneo.'
          )
        } else {
          this.enrollmentError.set('Non è stato possibile iscriversi al torneo.')
        }

        this.enrollmentLoading.set(false)
      }
    })
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
