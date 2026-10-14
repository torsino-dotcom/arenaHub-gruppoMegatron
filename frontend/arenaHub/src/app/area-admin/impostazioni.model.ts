export interface PlatformSettingsApi {
  id_impostazioni: number
  max_tornei_attivi_utente: number
  aggiornato_il: string
}

export interface UpdatePlatformSettingsApi {
  limite_iscrizioni_default: number
}

/**
 * max_tornei_attivi_utente e limite_iscrizioni_default
 * sono tecnicamente la stessa cosa. Si chiamano in modo diverso 
 * per vari errori di comunicazione.
 */