import { Component, input, output } from '@angular/core';
import { GameCardData } from '../../game-card-data';
import { RouterLink } from '@angular/router';
import { GameCardComponent } from '../game-card.component/game-card.component';

@Component({
  imports: [RouterLink, GameCardComponent],
  selector: 'app-game-grid',
  standalone: true,
  styleUrl: './game-grid.component.scss',
  templateUrl: './game-grid.component.html',
})
export class GameGridComponent {
  games = input.required<GameCardData[]>()

  gameSelected = output<GameCardData>()

}
