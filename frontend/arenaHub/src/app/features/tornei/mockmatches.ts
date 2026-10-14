import { TournamentMatch } from './tornei.model'

export interface MockTournamentParticipant {
  id_tornei: number
  id_utenti: number
  username: string
}

export const MOCK_TOURNAMENT_PARTICIPANTS: MockTournamentParticipant[] = [
  { id_tornei: 1, id_utenti: 1, username: 'Mario Rossi' },
  { id_tornei: 1, id_utenti: 2, username: 'Luca Bianchi' },
  { id_tornei: 1, id_utenti: 3, username: 'Giulia Verdi' },
  { id_tornei: 1, id_utenti: 4, username: 'Marco Neri' },
  { id_tornei: 2, id_utenti: 5, username: 'PhoenixPlayer' },
  { id_tornei: 2, id_utenti: 6, username: 'NovaPlayer' },
  { id_tornei: 3, id_utenti: 7, username: 'Elena Blu' },
  { id_tornei: 3, id_utenti: 8, username: 'Davide Rosso' },
]

export const MOCK_MATCHES: TournamentMatch[] = [
  {
    id_partite: 101,
    id_tornei: 1,
    id_utenti1: 1,
    id_utenti2: 2,
    orario_inizio: '2026-10-16T15:00:00',
    punteggio_1: null,
    punteggio_2: null,
    id_utente_vincitore: null,
    formato_match: 'eliminazione diretta',
    stato: 'programmata',
    fase: 'Quarti di finale',
  },
  {
    id_partite: 102,
    id_tornei: 1,
    id_utenti1: 3,
    id_utenti2: 4,
    orario_inizio: '2026-10-16T16:30:00',
    punteggio_1: null,
    punteggio_2: null,
    id_utente_vincitore: null,
    formato_match: 'eliminazione diretta',
    stato: 'programmata',
    fase: 'Quarti di finale',
  },
  {
    id_partite: 201,
    id_tornei: 2,
    id_utenti1: 5,
    id_utenti2: 6,
    orario_inizio: '2026-10-20T18:00:00',
    punteggio_1: null,
    punteggio_2: null,
    id_utente_vincitore: null,
    formato_match: 'best of 3',
    stato: 'programmata',
    fase: 'Semifinale',
  },
  {
    id_partite: 301,
    id_tornei: 3,
    id_utenti1: 7,
    id_utenti2: 8,
    orario_inizio: '2026-10-22T16:00:00',
    punteggio_1: null,
    punteggio_2: null,
    id_utente_vincitore: null,
    formato_match: 'best of 3',
    stato: 'programmata',
    fase: 'Quarti di finale',
  },
]