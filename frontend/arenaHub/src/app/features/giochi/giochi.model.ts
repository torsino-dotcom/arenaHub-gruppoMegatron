export interface Game {
    id_giochi: number
    nome_giochi: string
    url_img?: string | null
}

export interface GameListParams {
    page?: number
    limit?: number
}