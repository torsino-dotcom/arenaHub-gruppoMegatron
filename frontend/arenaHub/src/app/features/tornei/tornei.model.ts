
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

export interface TouranmentDetail extends TournamentListItem {
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