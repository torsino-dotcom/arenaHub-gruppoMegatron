import { Injectable, signal } from "@angular/core";
import { TournamentDetail } from "./tornei.model";
import { MOCK_TOURNAMENTS } from "./mocktournaments";

export type NewTournament = Omit<
    TournamentDetail,
    'id_tornei' | 'posti_occupati'
>

@Injectable({ providedIn: 'root' })
export class TournamentStoreService {
    private readonly items = signal<TournamentDetail[]>([...MOCK_TOURNAMENTS])

    readonly tournaments = this.items.asReadonly()

    findById(id: number): TournamentDetail | null {
        return this.items().find(tournament => tournament.id_tornei === id) ?? null
    }

    create(data: NewTournament): TournamentDetail {
        const id = Math.max(0, ...this.items().map(tournament => tournament.id_tornei)) + 1

        const tournament: TournamentDetail = {
            ...data,
            id_tornei: id,
            posti_occupati: 0
        }

        this.items.update(tournaments => [...tournaments, tournament])

        return tournament
    }

    delete(id: number): void {
        this.items.update(tournaments =>
            tournaments.filter(tournament => tournament.id_tornei !== id),
        )
    }
}