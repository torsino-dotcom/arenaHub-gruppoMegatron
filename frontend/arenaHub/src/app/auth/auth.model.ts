export type UserRoleApi = 'utente' | 'admin'

export interface AuthUserApi {
  id_utenti: number
  nome: string
  cognome: string
  username: string
  email: string
  ruolo: UserRoleApi
}

export interface LoginApiRequest {
  email: string
  password: string
}

export interface RegisterApiRequest {
  nome: string
  cognome: string
  username: string
  email: string
  password: string
}

export interface LogoutApiResponse {
  messaggio: string
}