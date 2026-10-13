import { Component, computed, input } from '@angular/core';
import { MOCK_MATCHES } from '../mockmatches';
import { MatchStatus } from '../tornei.model';
import { DatePipe } from '@angular/common';


@Component({
  imports: [DatePipe],
  selector: 'app-tournament-matches',
  standalone: true,
  styleUrl: './tournament-matches.component.scss',
  templateUrl: './tournament-matches.component.html',
})
export class TournamentMatchesComponent {
  readonly tournamentId = input.required<number>()

  readonly matches = computed(() => {
    const now = Date.now()

    return MOCK_MATCHES
      .filter(match => match.id_tornei === this.tournamentId())
      .filter(match => {
        const startTime = new Date(match.orario_inizio).getTime()

        return match.stato === 'in_corso' ||
          (match.stato === 'programmata' && startTime >= now)
      })
      .sort((first, second) =>
        new Date(first.orario_inizio).getTime() -
        new Date(second.orario_inizio).getTime()
      )
  })

  statusLabel(status: MatchStatus): string {
    switch (status) {
      case 'in_corso':
        return 'In corso'
      case 'conclusa':
        return 'Conclusa'
      default:
        return 'Programmata'
    }
  }
}
