import { Component, input } from '@angular/core';
import { TournamentListItem } from '../tornei.model';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';

@Component({
  imports: [RouterLink, DatePipe],
  selector: 'app-tournament-card',
  styleUrl: './tournament-card.component.scss',
  templateUrl: './tournament-card.component.html',
})
export class TournamentCardComponent {
  tournament = input.required<TournamentListItem>()
}
