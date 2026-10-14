import { Injectable, signal } from "@angular/core";
import { TournamentMatch, TournamentMatchData } from "./tornei.model";
import { MOCK_MATCHES } from "./mockmatches";

@Injectable({ providedIn: 'root' })
export class TournamentMatchStoreService {
    private readonly items = signal<TournamentMatch[]>([...MOCK_MATCHES])

    readonly matches = this.items.asReadonly()

    forTournament(tournamentId: number): TournamentMatch[] {
        return this.items().filter(match => match.id_tornei === tournamentId)
    }
    
    create(tournamentId: number, data: TournamentMatchData): TournamentMatch {
        const id = Math.max(0, ...this.items().map(match => match.id_partite)) + 1

        const match: TournamentMatch = {
            ...data,
            id_partite: id,
            id_tornei: tournamentId
        }

        this.items.update(matches => [...matches, match])
        return match
    } 

    update(id: number, data: TournamentMatchData): TournamentMatch | null {
        const existing = this.items().find(match => match.id_partite === id)

        if(!existing) return null

        const updated = {...existing, ...data}

        this.items.update(matches =>
            matches.map(match => match.id_partite === id ? updated : match)
        )

        return updated
    }

    delete(id: number): void {
        this.items.update(matches =>
            matches.filter(match => match.id_partite !== id)
        )
    }
}