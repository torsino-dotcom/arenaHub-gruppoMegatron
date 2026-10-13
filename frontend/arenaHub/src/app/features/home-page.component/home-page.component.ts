import { ViewportScroller } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { GameCardData } from '../../shared/game-card-data';
import { HomeHeroComponent } from '../../shared/components/home-hero.component/home-hero.component';
import { GameGridComponent } from '../../shared/components/game-grid.component/game-grid.component';

@Component({
  imports: [HomeHeroComponent, GameGridComponent],
  selector: 'app-home-page.component',
  styleUrl: './home-page.component.scss',
  templateUrl: './home-page.component.html',
})
export class HomePageComponent {
  private viewportScroller = inject(ViewportScroller)

  //! dati provvisori
  games = signal<GameCardData[]>([
    { id: 'league-of-legends', name: 'League of Legends' },
    { id: 'valorant', name: 'Valorant' },
    { id: 'tetris', name: 'Tetris' },
    { id: 'fortnite', name: 'Fortnite' },
  ])

  selectedGame = signal<GameCardData | null>(null)

  scrollToGames(): void {
    this.viewportScroller.scrollToAnchor("games")
  }

  selectGame(game: GameCardData): void {
    this.selectedGame.set(game)
  }
}
