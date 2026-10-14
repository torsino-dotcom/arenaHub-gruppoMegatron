import { Component, computed, inject, input, signal } from '@angular/core';
import { MOCK_MATCHES, MOCK_TOURNAMENT_PARTICIPANTS } from '../mockmatches';
import { MatchStatus, TournamentMatch, TournamentMatchData } from '../tornei.model';
import { DatePipe } from '@angular/common';
import { TournamentMatchStoreService } from '../tournament-match-store.service';

interface MatchForm {
  id_utenti1: number | null
  id_utenti2: number | null
  orario_inizio: string
  punteggio_1: number | null
  punteggio_2: number | null
  id_utente_vincitore: number | null
  formato_match: string
  stato: MatchStatus
  fase: string
}

@Component({
  imports: [DatePipe],
  selector: 'app-tournament-matches',
  standalone: true,
  styleUrl: './tournament-matches.component.scss',
  templateUrl: './tournament-matches.component.html',
})
export class TournamentMatchesComponent {
  private readonly matchStore = inject(TournamentMatchStoreService)

  readonly tournamentId = input.required<number>()
  readonly isAdmin = input(false)

  readonly formOpen = signal(false)
  readonly editingMatchId = signal<number | null>(null)
  readonly errorMessage = signal<string | null>(null)

  readonly participants = computed(() =>
    MOCK_TOURNAMENT_PARTICIPANTS.filter(
      participant => participant.id_tornei === this.tournamentId()
    )
  )

  readonly matches = computed(() => {
    const matches = this.matchStore.forTournament(this.tournamentId())
    const now = Date.now()

    return matches
      .filter(match =>
        this.isAdmin() ||
        match.stato === 'in_corso' ||
        (match.stato === 'programmata' &&
          new Date(match.orario_inizio).getTime() >= now)
      ).sort((first, second) =>
        new Date(first.orario_inizio).getTime() -
        new Date(second.orario_inizio).getTime()
      )
  })

  readonly form = signal<MatchForm>(this.emptyForm())

  setParticipant(field: 'id_utenti1' | 'id_utenti2',
    event: Event): void {
    const value = (event.target as HTMLSelectElement).value

    this.form.update(current => ({
      ...current,
      [field]: value ? Number(value) : null,
    }))
  }

  setNumber(
    field: 'punteggio_1' | 'punteggio_2',
    event: Event
  ): void {
    const value = (event.target as HTMLInputElement).value

    this.form.update(current => ({
      ...current,
      [field]: value === '' ? null : Number(value),
    }))
  }

  setText(
    field: 'orario_inizio' | 'formato_match' | 'fase',
    event: Event
  ): void {
    const value = (event.target as HTMLInputElement).value

    this.form.update(current => ({
      ...current,
      [field]: value,
    }))
  }

  setWinner(event: Event): void {
    const value = (event.target as HTMLSelectElement).value

    this.form.update(current => ({
      ...current,
      id_utente_vincitore: value ? Number(value) : null,
    }))
  }

  setStatus(event: Event): void {
    const stato = (event.target as HTMLSelectElement).value as MatchStatus

    this.form.update(current => ({
      ...current,
      stato,
      id_utente_vincitore: stato === 'conclusa'
        ? current.id_utente_vincitore
        : null,
    }))
  }

  openCreateForm(): void {
    this.form.set(this.emptyForm())
    this.editingMatchId.set(null)
    this.errorMessage.set(null)
    this.formOpen.set(true)
  }

  editMatch(match: TournamentMatch): void {
    this.form.set({
      id_utenti1: match.id_utenti1,
      id_utenti2: match.id_utenti2,
      orario_inizio: match.orario_inizio.slice(0, 16),
      punteggio_1: match.punteggio_1,
      punteggio_2: match.punteggio_2,
      id_utente_vincitore: match.id_utente_vincitore,
      formato_match: match.formato_match,
      stato: match.stato,
      fase: match.fase,
    })
    this.editingMatchId.set(match.id_partite)
    this.errorMessage.set(null)
    this.formOpen.set(true)
  }

  cancelForm(): void {
    this.formOpen.set(false)
    this.editingMatchId.set(null)
    this.errorMessage.set(null)
    this.form.set(this.emptyForm())
  }

  saveMatch(event: Event): void {
    event.preventDefault()
    this.errorMessage.set(null)

    const form = this.form()
    const { id_utenti1, id_utenti2, id_utente_vincitore } = form

    if (id_utenti1 === null || id_utenti2 === null) {
      this.errorMessage.set('Seleziona entrambi i partecipanti.')
      return
    }

    if (id_utenti1 === id_utenti2) {
      this.errorMessage.set('I due partecipanti devono essere diversi.')
      return
    }

    if (
      id_utente_vincitore !== null &&
      id_utente_vincitore !== id_utenti1 &&
      id_utente_vincitore !== id_utenti2
    ) {
      this.errorMessage.set('Il vincitore deve essere uno dei due partecipanti.')
      return
    }

    const data: TournamentMatchData = {
      ...form,
      id_utenti1,
      id_utenti2,
      orario_inizio: form.orario_inizio.length === 16
        ? `${form.orario_inizio}:00`
        : form.orario_inizio,
    }

    const editingId = this.editingMatchId()

    if (editingId !== null) {
      const updated = this.matchStore.update(editingId, data)

      if (!updated) {
        this.errorMessage.set('La partita non è stata trovata.')
        return
      }
    } else {
      this.matchStore.create(this.tournamentId(), data)
    }

    this.cancelForm()
  }

  deleteMatch(match: TournamentMatch): void {
    const confirmed = window.confirm(
      `Vuoi eliminare la partita ${this.participantName(match.id_utenti1)} vs ${this.participantName(match.id_utenti2)}?`
    )

    if (confirmed) {
      this.matchStore.delete(match.id_partite)

      if (this.editingMatchId() === match.id_partite) {
        this.cancelForm()
      }
    }
  }

  participantName(userId: number): string {
    return this.participants().find(
      participant => participant.id_utenti === userId
    )?.username ?? `Utente #${userId}`
  }

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

  private emptyForm(): MatchForm {
    return {
      id_utenti1: null,
      id_utenti2: null,
      orario_inizio: '',
      punteggio_1: null,
      punteggio_2: null,
      id_utente_vincitore: null,
      formato_match: '',
      stato: 'programmata',
      fase: '',
    }
  }
}
