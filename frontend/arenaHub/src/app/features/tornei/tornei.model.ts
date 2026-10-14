
/**
 * definisce lo stato di un torneo
 */
export type TournamentStatus =
    | 'bozza'
    | 'in_corso'
    | 'aperto'
    | 'chiuso'
    | 'concluso'
/**
 * definisce lo stato di un iscrizione
 */
export type EnrollmentStatus =
    | 'confermato'
    | 'in_attesa'
    | 'annullato'

export interface TournamentListItem {
    id_tornei: number
    nome_torneo: string
    descrizione: string
    gioco: string
    data_ora: string
    data_inizio_iscrizioni: string
    data_fine_iscrizioni: string
    posti_max: number
    stato: TournamentStatus
}

export interface TournamentDetail extends TournamentListItem {
    posti_occupati: number
}

export interface TournamentEnrollment {
    id_utenti_tornei: number
    id_utenti: number
    id_tornei: number
    data_iscrizione: string
    stato: EnrollmentStatus
    username?: string
    nome?: string
    cognome?: string
}

export interface TournamentFilters {
    gioco?: string
    stato?: TournamentStatus
    data_da?: string
    data_a?: string
    q?: string
    page?: number
    limit?: number
}

export type MatchStatus = 'programmata' | 'in_corso' | 'conclusa'

export interface TournamentMatch {
  id_partite: number
  id_tornei: number
  id_utenti1: number
  id_utenti2: number
  orario_inizio: string
  punteggio_1: number | null
  punteggio_2: number | null
  id_utente_vincitore: number | null
  formato_match: string
  stato: MatchStatus
  fase: string
}

export type TournamentMatchData = Omit<
  TournamentMatch,
  'id_partite' | 'id_tornei'
>

export interface TournamentApiItem {
  id_tornei: number
  nome_torneo: string
  descrizione: string
  id_giochi: number
  data_ora: string
  data_inizio_iscrizioni: string
  data_fine_iscrizioni: string
  posti_max: number
  stato: TournamentStatus
}

export interface TournamentApiDetail extends TournamentApiItem {
  posti_occupati: number
}

export type NewTournamentApi = Omit<TournamentApiItem, 'id_tornei'>

export type TournamentPatchApi = Partial<NewTournamentApi>

export interface TournamentApiFilters {
  gioco?: string
  stato?: TournamentStatus
  data_da?: string
  data_a?: string
  q?: string
  page?: number
  limit?: number
}

export interface ApiMessage {
  messaggio: string
}

export type EnrollmentApiStatus = 'confermato' | 'in_coda' | 'annullato'

export interface TournamentEnrollmentApi {
  id_utenti_tornei: number
  id_utenti: number
  id_tornei: number
  data_iscrizione: string
  stato: EnrollmentApiStatus
  username?: string
  nome?: string
  cognome?: string
}

export interface MyTournamentEnrollmentApi extends TournamentEnrollmentApi {
  nome_torneo: string
  id_giochi: number
  data_ora: string
}

export interface TournamentEnrollmentsResponse {
  iscrizioni: TournamentEnrollmentApi[]
}

export interface TournamentMatchApi {
  id_partite: number
  id_tornei: number
  id_utenti1: number
  id_utenti2: number
  orario_inizio: string
  punteggio_1: number | null
  punteggio_2: number | null
  id_persona_vincitrice: number | null
  formato_match: string
  stato: MatchStatus
  fase: string
}

export type NewTournamentMatchApi = Omit<
  TournamentMatchApi,
  'id_partite' | 'id_tornei'
>

export interface TournamentMatchFilters {
  fase?: string
  stato?: MatchStatus
}

export interface TournamentEnrollmentsResponse {
  iscrizioni: TournamentEnrollmentApi[]
}

export type AdminEnrollmentStatus = 'confermato' | 'annullato'

export interface UpdateEnrollmentStatusApi {
  stato: AdminEnrollmentStatus
}

