import { ViewportScroller } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { GameCardData } from '../../shared/game-card-data';
import { HomeHeroComponent } from '../../shared/components/home-hero.component/home-hero.component';
import { GameGridComponent } from '../../shared/components/game-grid.component/game-grid.component';
import { HomeTournamentsPreviewComponent } from '../home-tournaments-preview.component/home-tournaments-preview.component';
import { GiochiApiService } from '../giochi/giochi-api.service';
import { toSignal } from '@angular/core/rxjs-interop';
import { catchError, map, of } from 'rxjs';

@Component({
  imports: [HomeHeroComponent, GameGridComponent, HomeTournamentsPreviewComponent],
  selector: 'app-home-page.component',
  styleUrl: './home-page.component.scss',
  templateUrl: './home-page.component.html',
})
export class HomePageComponent {
  private readonly giochiApi = inject(GiochiApiService)
  private viewportScroller = inject(ViewportScroller)
  private router = inject(Router);

  readonly gamesError = signal<string | null>(null)

  //! dati provvisori
  // games = signal<GameCardData[]>([
  //   { id: 'league-of-legends', name: 'League of Legends', imageUrl: 'img/leagueoflegends.jpg' },
  //   { id: 'valorant', name: 'Valorant', imageUrl: 'img/valorant.jpg' },
  //   { id: 'tetris', name: 'Tetris', imageUrl: 'img/tetris.jpg' },
  //   { id: 'fortnite', name: 'Fortnite', imageUrl: 'img/fortnite.jpg' },
  // ])
  readonly games = toSignal(
    this.giochiApi.getAll().pipe(
      map(apiGames =>
        apiGames.map(game => ({
          id: game.id_giochi,
          name: game.nome_giochi,
          imageUrl: game.url_img ?? ''
        }))
      ),
      catchError(() => {
        this.gamesError.set('Non è stato possibile caricare i giochi.')
        return of([] as GameCardData[])
      })
    ),
    {initialValue: [] as GameCardData[]}
  )

  selectedGame = signal<GameCardData | null>(null)

  scrollToGames(): void {
    this.viewportScroller.scrollToAnchor("games")
  }

  selectGame(game: GameCardData): void {
    this.selectedGame.set(game)

    this.router.navigate(['/tornei'], { queryParams: { game: game.name } });
  }
}
