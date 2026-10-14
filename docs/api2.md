# API ArenaHub

Gli endpoint e i campi descritti qui seguono le tabelle mostrate in `arenaHub.drawio`. I nomi dei campi JSON corrispondono alle colonne, in formato `snake_case`.

> **Nota sullo schema:** `db/schema.sql` al momento non coincide completamente con il diagramma. Per esempio, nel diagramma `utenti` contiene `username` e `tornei` contiene `nome_torneo`, mentre lo SQL non li riporta o usa nomi diversi. Prima di implementare queste API, allineare lo schema SQL al diagramma.

## Convenzioni generali

- **Formato:** JSON (`Content-Type: application/json`). Date in `YYYY-MM-DD`, data e ora in ISO 8601 (`YYYY-MM-DDTHH:MM:SS`).
- **Autenticazione:** sessione (cookie) creata da `POST /api/auth/login`. `id_utenti` e `ruolo` si ricavano sempre dall'utente autenticato, mai dal body.
- **Ruoli** (colonna `ruolo`): `utente`, `admin`.
- **Password:** salvata solo come hash; non viene mai restituita nelle risposte.
- **Valori di `stato`:**
  - `tornei.stato`: `aperto`, `chiuso`, `concluso`
  - `utenti_tornei.stato`: `Confermato`, `in_coda`
  - `partite.stato`: `programmata`, `in_corso`, `conclusa`
- **Paginazione (liste):** parametri facoltativi `page` (default 1) e `limit` (default 20).
- **Errori:** formato unico

```json
{ "errore": "Descrizione del problema" }
```

| Codice | Significato |
|---|---|
| 400 | Richiesta malformata o campi non validi |
| 401 | Non autenticato |
| 403 | Autenticato ma senza permessi |
| 404 | Risorsa non trovata |
| 409 | Conflitto (es. già iscritto, username/email già usati) |
| 422 | Regola di business violata (es. iscrizioni chiuse) |

---

## Autenticazione

### 1. Registrazione

#### `POST /api/auth/register`

Accesso: pubblico. Il `ruolo` viene sempre impostato a `utente` dal server.

**Body**

```json
{
  "nome": "Mario",
  "cognome": "Rossi",
  "username": "mrossi",
  "email": "mario.rossi@example.com",
  "password": "********"
}
```

**Risposta 201**

```json
{
  "id_utenti": 12,
  "nome": "Mario",
  "cognome": "Rossi",
  "username": "mrossi",
  "email": "mario.rossi@example.com",
  "ruolo": "utente"
}
```

Errori: `400` campi mancanti/non validi, `409` `username` o `email` già in uso.

### 2. Login

#### `POST /api/auth/login`

Accesso: pubblico.

**Body**

```json
{ "email": "mrossi@gmail.com", "password": "********" }
```

**Risposta 200:** restituisce l'utente (come in registrazione) e imposta il cookie di sessione. Errore `401` se le credenziali non sono valide.

### 3. Logout

#### `POST /api/auth/logout`

Accesso: utente autenticato. **Risposta 200:** `{ "messaggio": "Logout effettuato" }`

---

## Utenti

### 4. Consultare il proprio profilo

#### `GET /api/utenti/me`

Accesso: utente autenticato. **Risposta 200:** campi di `utenti` tranne `password`.

### 5. Modificare il proprio profilo

#### `PATCH /api/utenti/me`

Accesso: utente autenticato. Campi modificabili: `nome`, `cognome`, `username`, `email`, `password`. `id_utenti` e `ruolo` non sono modificabili da questo endpoint. **Risposta 200:** utente aggiornato (senza `password`). Errore `409` se `username`/`email` sono già in uso.

### 6. Consultare le proprie iscrizioni

#### `GET /api/utenti/me/iscrizioni`

Accesso: utente autenticato. Restituisce le iscrizioni dell'utente con i dati essenziali del torneo.

**Risposta 200**

```json
[
  {
    "id_utenti_tornei": 21,
    "id_tornei": 3,
    "data_iscrizione": "2026-10-12",
    "stato": "confermato",
    "nome_torneo": "Torneo d'autunno",
    "id_giochi": 16,
    "data_ora": "2026-10-16T15:00:00"
  }
]
```

### 7. Elencare gli utenti

#### `GET /api/utenti`

Accesso: admin. Filtri facoltativi: `ruolo`, `username`. **Risposta 200:** lista di utenti (senza `password`).

### 8. Consultare un utente

#### `GET /api/utenti/{id_utenti}`

Accesso: admin. **Risposta 200:** campi di `utenti` tranne `password`.

### 9. Modificare ruolo di un utente

#### `PATCH /api/utenti/{id_utenti}`

Accesso: admin. Body: `{ "ruolo": "admin" }`. Campi ammessi: `ruolo`, `nome`, `cognome`, `username`, `email`.

### 10. Eliminare un utente

#### `DELETE /api/utenti/{id_utenti}`

Accesso: admin. Elimina l'utente e le sue iscrizioni in `utenti_tornei`. **Risposta 200:** `{ "messaggio": "Utente eliminato" }`

---

## Iscrizioni ai tornei

### 11. Consultare le iscrizioni a un torneo

#### `GET /api/tornei/{id_tornei}/iscrizioni`

Accesso: pubblico. Restituisce le iscrizioni collegate al torneo tramite `utenti_tornei`. Filtro facoltativo: `stato`.

Per privacy, il pubblico vede solo `username`; `nome` e `cognome` sono restituiti solo agli admin.

**Risposta 200**

```json
{
  "iscrizioni": [
    {
      "id_utenti_tornei": 21,
      "id_utenti": 12,
      "id_tornei": 3,
      "data_iscrizione": "2026-10-12",
      "stato": "confermato",
      "username": "mrossi",
      "nome": "Mario",
      "cognome": "Rossi"
    }
  ]
}
```

`nome` e `cognome` compaiono solo se la richiesta è fatta da un admin. `password` e `email` non vengono mai incluse.

### 12. Iscriversi a un torneo

#### `POST /api/tornei/{id_tornei}/iscrizioni`

Accesso: utente autenticato. Nessun body. `id_utenti` è ricavato dalla sessione; il server registra `data_iscrizione` e assegna `stato` in base ai posti disponibili.

Regole:
- il torneo deve avere `stato: "aperto"` e la data odierna deve essere compresa tra `data_inizio_iscrizioni` e `data_fine_iscrizioni`, altrimenti `422`;
- l'utente non può essere già iscritto (iscrizione non annullata), altrimenti `409`;
- se gli iscritti `confermato` hanno raggiunto `posti_max`, l'iscrizione viene registrata con `stato: "in_coda"`.

**Risposta 201**

```json
{
  "id_utenti_tornei": 21,
  "id_utenti": 12,
  "id_tornei": 3,
  "data_iscrizione": "2026-10-12",
  "stato": "confermato"
}
```

### 13. Annullare la propria iscrizione

#### `DELETE /api/tornei/{id_tornei}/iscrizioni`

Accesso: utente autenticato. Annulla l'iscrizione dell'utente autenticato, identificata dalla coppia `id_utenti` e `id_tornei`. Se si libera un posto, la prima iscrizione `in_coda` (per `data_iscrizione`) passa a `confermato`.

**Risposta 200**

```json
{ "messaggio": "Iscrizione annullata" }
```

Errore `404` se l'utente non è iscritto.

### 14. Modificare lo stato di un'iscrizione (admin)

#### `PATCH /api/tornei/{id_tornei}/iscrizioni/{id_utenti_tornei}`

Accesso: admin. Permette di confermare un'iscrizione in coda o annullarla. Body: `{ "stato": "confermato" }`. Errore `422` se si supera `posti_max`. **Risposta 200:** iscrizione aggiornata.

### 15. Rimuovere un'iscrizione (admin)

#### `DELETE /api/tornei/{id_tornei}/iscrizioni/{id_utenti_tornei}`

Accesso: admin. Rimuove l'iscrizione di un qualsiasi utente. **Risposta 200:** `{ "messaggio": "Iscrizione rimossa" }`

---

## Tornei

### 16. Consultare e filtrare i tornei

#### `GET /api/tornei`

Accesso: pubblico. Filtri facoltativi: `gioco`, `stato`, `data_da`, `data_a` (intervallo su `data_ora`), `q` (ricerca su `nome_torneo`). Ordinamento di default: `data_ora` crescente. Le bozze sono visibili solo agli admin.

**Risposta 200**

```json
[
  {
    "id_tornei": 3,
    "nome_torneo": "Torneo d'autunno",
    "descrizione": "Torneo amatoriale",
    "id_giochi": 16,
    "data_ora": "2026-10-16T15:00:00",
    "data_inizio_iscrizioni": "2026-10-01",
    "data_fine_iscrizioni": "2026-10-15",
    "posti_max": 16,
    "stato": "aperto"
  }
]
```

### 17. Consultare il dettaglio di un torneo

#### `GET /api/tornei/{id_tornei}`

Accesso: pubblico. Restituisce i campi della tabella `tornei` più il campo calcolato `posti_occupati` (iscrizioni `confermato`, non presente nella tabella).

**Risposta 200**

```json
{
  "id_tornei": 3,
  "nome_torneo": "Torneo d'autunno",
  "descrizione": "Torneo amatoriale",
  "id_giochi": 16,
  "data_ora": "2026-10-16T15:00:00",
  "data_inizio_iscrizioni": "2026-10-01",
  "data_fine_iscrizioni": "2026-10-15",
  "posti_max": 16,
  "stato": "aperto",
  "posti_occupati": 9
}
```

### 18. Creare un torneo

#### `POST /api/tornei`

Accesso: admin. `id_tornei` viene generato dal database. Validazioni: `data_inizio_iscrizioni` ≤ `data_fine_iscrizioni` ≤ `data_ora`; `posti_max` > 0.

**Body**

```json
{
  "nome_torneo": "Torneo d'autunno",
  "descrizione": "Torneo amatoriale",
  "id_giochi": 16,
  "data_ora": "2026-10-16T15:00:00",
  "data_inizio_iscrizioni": "2026-10-01",
  "data_fine_iscrizioni": "2026-10-15",
  "posti_max": 16,
  "stato": "aperto"
}
```

**Risposta 201:** restituisce il torneo creato, inclusi tutti i campi della tabella `tornei`.

### 19. Modificare un torneo

#### `PATCH /api/tornei/{id_tornei}`

Accesso: admin. Il body può contenere uno o più campi modificabili di `tornei`: `nome_torneo`, `descrizione`, `id_giochi`, `data_ora`, `data_inizio_iscrizioni`, `data_fine_iscrizioni`, `posti_max`, `stato`. `id_tornei` non è modificabile. `posti_max` non può scendere sotto il numero di iscritti confermati (`422`).

**Risposta 200:** torneo aggiornato.

### 20. Eliminare un torneo

#### `DELETE /api/tornei/{id_tornei}`

Accesso: admin. Elimina il torneo identificato da `id_tornei` insieme alle sue iscrizioni (`utenti_tornei`) e partite (`partite`) collegate (cancellazione a cascata). **Risposta 200:** `{ "messaggio": "Torneo eliminato" }`

---

## Partite

### 21. Consultare le partite di un torneo

#### `GET /api/tornei/{id_tornei}/partite`

Accesso: pubblico. Restituisce le righe di `partite` associate al torneo. Filtri facoltativi: `fase`, `stato`.

**Risposta 200**

```json
[
  {
    "id_partite": 101,
    "id_tornei": 3,
    "id_utenti1": 10,
    "id_utenti2": 11,
    "orario_inizio": "2026-10-16T15:00:00",
    "punteggio_1": 2,
    "punteggio_2": 1,
    "id_persona_vincitrice": 10,
    "formato_match": "eliminazione diretta",
    "stato": "conclusa",
    "fase": "quarti"
  }
]
```

### 22. Consultare il dettaglio di una partita

#### `GET /api/partite/{id_partite}`

Accesso: pubblico. Restituisce i campi della tabella `partite` per la partita richiesta, con la stessa struttura dell'esempio precedente (singolo oggetto). Errore `404` se non esiste.

### 23. Creare una partita

#### `POST /api/tornei/{id_tornei}/partite`

Accesso: admin. `id_partite` viene generato dal database e `id_tornei` è ricavato dal percorso.

**Body**

```json
{
  "id_utenti1": 10,
  "id_utenti2": 11,
  "orario_inizio": "2026-10-16T15:00:00",
  "punteggio_1": 0,
  "punteggio_2": 0,
  "id_persona_vincitrice": null,
  "formato_match": "eliminazione diretta",
  "stato": "programmata",
  "fase": "quarti"
}
```

**Risposta 201:** restituisce la partita creata.

### 24. Aggiornare risultato o stato di una partita

#### `PATCH /api/partite/{id_partite}`

Accesso: admin. Il body può contenere i campi da aggiornare: `id_utenti1`, `id_utenti2`, `orario_inizio`, `punteggio_1`, `punteggio_2`, `id_persona_vincitrice`, `formato_match`, `stato`, `fase`. `id_partite` e `id_tornei` non sono modificabili da questo endpoint. Se presente, `persona_vincitrice` deve coincidere con `nome_persona1` o `nome_persona2` (`422` altrimenti).

**Body di esempio per registrare il risultato**

```json
{
  "punteggio_1": 2,
  "punteggio_2": 1,
  "id_persona_vincitrice": 11,
  "stato": "conclusa"
}
```

**Risposta 200:** partita aggiornata.

### 25. Eliminare una partita

#### `DELETE /api/partite/{id_partite}`

Accesso: admin. **Risposta 200:** `{ "messaggio": "Partita eliminata" }`

I nomi dei partecipanti sono testuali nel diagramma; non sono mostrati campi `id_utenti` per collegare una partita agli utenti.

### 26. Consultare un gioco
GET /api/giochi/{id_giochi}
Accesso: pubblico. Restituisce l'elenco di tutti i giochi disponibili nel sistema (collegati alla tabella giochi del diagramma). Supporta la paginazione (page, limit).

Risposta 200


```json
  {
    "id_giochi": 16,
    "nome_giochi": "League of Legends",
    "url_img": "https://example.com/images/lol.png"
  }
```

#### 27 `GET /api/giochi`

Accesso: pubblico. Restituisce l'elenco completo di tutti i giochi registrati sulla piattaforma, riprendendo i campi della tabella giochi del diagramma ER (id_gioco, nome_giochi, url_img). Supporta la paginazione facoltativa (page, limit).

Risposta 200


```json
[
  {
    "id_giochi": 16,
    "nome_giochi": "League of Legends",
  },
  {
    "id_giochi": 17,
    "nome_giochi": "Valorant",
  }
]
```

### 28 `GET  /api/impostazioni`
GET /api/impostazioni
Accesso: pubblico. Restituisce i parametri di configurazione globali della piattaforma (come il limite massimo di iscrizioni), in modo tale che il client possa mostrare quanti posti o iscrizioni restano disponibili.

Risposta 200

```json
{
  "id_impostazioni": 1,
  "max_tornei_attivi_utente": 3,
  "aggiornato_il": "2026-10-17T15:00:00"
}
```

### 29 `PATCH /api/impostazioni`
Accesso: admin. Permette a un amministratore di aggiornare i parametri di configurazione globali. Richiede l'autorizzazione admin e valida che il valore passato sia un intero/parametro ammesso.

Body


{
  "limite_iscrizioni_default": 32
}
Risposta 200

```json
{
  "id_impostazioni": 1,
  "max_tornei_attivi_utente": 3,
  "aggiornato_il": "2026-10-17T15:00:00"
}
```
Errori: 400 per valori non validi, 403 se l'utente non è admin.

## Riepilogo endpoint

| # | Metodo | Percorso | Accesso |
|---|---|---|---|
| 1 | POST | `/api/auth/register` | pubblico |
| 2 | POST | `/api/auth/login` | pubblico |
| 3 | POST | `/api/auth/logout` | autenticato |
| 4 | GET | `/api/utenti/me` | autenticato |
| 5 | PATCH | `/api/utenti/me` | autenticato |
| 6 | GET | `/api/utenti/me/iscrizioni` | autenticato |
| 7 | GET | `/api/utenti` | admin   |
| 8 | GET | `/api/utenti/{id_utenti}` | admin |
| 9 | PATCH | `/api/utenti/{id_utenti}` | admin |
| 10 | DELETE | `/api/utenti/{id_utenti}` | admin |
| 11 | GET | `/api/tornei/{id_tornei}/iscrizioni` | admin |
| 12 | POST | `/api/tornei/{id_tornei}/iscrizioni` | autenticato |
| 13 | DELETE | `/api/tornei/{id_tornei}/iscrizioni` | autenticato |
| 14 | PATCH | `/api/tornei/{id_tornei}/iscrizioni/{id_utenti_tornei}` | admin |
| 15 | DELETE | `/api/tornei/{id_tornei}/iscrizioni/{id_utenti_tornei}` | admin |
| 16 | GET | `/api/tornei` | pubblico |
| 17 | GET | `/api/tornei/{id_tornei}` | pubblico |
| 18 | POST | `/api/tornei` | admin |
| 19 | PATCH | `/api/tornei/{id_tornei}` | admin |
| 20 | DELETE | `/api/tornei/{id_tornei}` | admin |
| 21 | GET | `/api/tornei/{id_tornei}/partite` | pubblico |
| 22 | GET | `/api/partite/{id_partite}` | pubblico |
| 23 | POST | `/api/tornei/{id_tornei}/partite` | admin |
| 24 | PATCH | `/api/partite/{id_partite}` | admin |
| 25 | DELETE | `/api/partite/{id_partite}` | admin |
| 26 | GET |  `GET /api/giochi/{id_giochi}` | pubblico |
| 27 | GET |  `GET /api/giochi` |  pubblico |
| 28 | GET |  `/api/impostazioni` |  pubblico |
| 29 | PATCH | `/api/impostazioni` |  admin |