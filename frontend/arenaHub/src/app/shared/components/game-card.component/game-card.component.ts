import { Component, input, output } from '@angular/core';
import { GameCardData } from '../../game-card-data';

@Component({
  imports: [],
  selector: 'app-game-card',
  standalone: true,
  styleUrl: './game-card.component.scss',
  templateUrl: './game-card.component.html',
})
export class GameCardComponent {
  game = input.required<GameCardData>()

  selected = output<GameCardData>()

  selectGame(): void {
    this.selected.emit(this.game())
  }
}
