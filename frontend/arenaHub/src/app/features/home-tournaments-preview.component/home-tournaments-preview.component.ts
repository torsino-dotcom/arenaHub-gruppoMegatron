import { Component, inject, signal } from '@angular/core';
import { TournamentListItem } from '../tornei/tornei.model';
import { RouterLink } from '@angular/router';
import { TournamentCardComponent } from '../tornei/tournament-card.component/tournament-card.component';
import { TorneiApiService } from '../tornei/services/tornei-api.service';
import { GiochiApiService } from '../giochi/giochi-api.service';
import { toSignal } from '@angular/core/rxjs-interop';
import { catchError, combineLatest, finalize, map, of } from 'rxjs';

@Component({
  imports: [RouterLink, TournamentCardComponent],
  selector: 'app-home-tournaments-preview',
  styleUrl: './home-tournaments-preview.component.scss',
  templateUrl: './home-tournaments-preview.component.html',
})
export class HomeTournamentsPreviewComponent {
  private readonly torneiApi = inject(TorneiApiService)
  private readonly giochiApi = inject(GiochiApiService)

  readonly loading = signal(true)
  readonly errorMessage = signal<string | null>(null)

  readonly tournaments = toSignal(
    combineLatest({
      tournaments: this.torneiApi.getAll(),
      games: this.giochiApi.getAll(),
    }).pipe(
      map(({ tournaments, games }) => {
        const gameNames = new Map(
          games.map(game => [game.id_giochi, game.nome_giochi])
        )

        const now = Date.now()

        return tournaments
          .filter(tournament =>
            tournament.stato === 'aperto' &&
            new Date(tournament.data_ora).getTime() >= now
          )
          .map(tournament => ({
            id_tornei: tournament.id_tornei,
            nome_torneo: tournament.nome_torneo,
            descrizione: tournament.descrizione,
            gioco: gameNames.get(tournament.id_giochi) ?? 'Gioco non disponibile',
            data_ora: tournament.data_ora,
            data_inizio_iscrizioni: tournament.data_inizio_iscrizioni,
            data_fine_iscrizioni: tournament.data_fine_iscrizioni,
            posti_max: tournament.posti_max,
            stato: tournament.stato,
          }))
          .sort((first, second) =>
            new Date(first.data_ora).getTime() -
            new Date(second.data_ora).getTime()
          )
          .slice(0, 3)
      }),
      catchError(() => {
        this.errorMessage.set('Non è stato possibile caricare i tornei.')
        return of([] as TournamentListItem[])
      }),
      finalize(() => this.loading.set(false))
    ),
    { initialValue: [] as TournamentListItem[] }
  )
}
