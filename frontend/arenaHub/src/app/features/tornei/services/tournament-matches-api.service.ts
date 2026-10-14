import { HttpClient, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { ApiMessage, NewTournamentMatchApi, TournamentMatchApi, TournamentMatchFilters } from "../tornei.model";
import { Observable } from "rxjs";

@Injectable({ providedIn: 'root' })
export class TournamentMatchesApiService {
    private readonly http = inject(HttpClient)
    private readonly tournamentsUrl = '/api/tornei'
    private readonly matchesUrl = '/api/partite'

    getForTournament(
        tournamentId: number,
        filters: TournamentMatchFilters = {}
    ): Observable<TournamentMatchApi[]> {
        let params = new HttpParams()

        if (filters.fase) {
            params = params.set('fase', filters.fase)
        }

        if (filters.stato) {
            params = params.set('stato', filters.stato)
        }

        return this.http.get<TournamentMatchApi[]>(
            `${this.tournamentsUrl}/${tournamentId}/partite`,
            { params }
        )
    }

    getById(matchId: number): Observable<TournamentMatchApi> {
        return this.http.get<TournamentMatchApi>(
            `${this.matchesUrl}/${matchId}`
        )
    }

    create(
        tournamentId: number,
        data: NewTournamentMatchApi
    ): Observable<TournamentMatchApi> {
        return this.http.post<TournamentMatchApi>(
            `${this.tournamentsUrl}/${tournamentId}/partite`,
            data
        )
    }

    update(
        matchId: number,
        data: Partial<NewTournamentMatchApi>
    ): Observable<TournamentMatchApi> {
        return this.http.patch<TournamentMatchApi>(
            `${this.matchesUrl}/${matchId}`,
            data
        )
    }

    delete(matchId: number): Observable<ApiMessage> {
        return this.http.delete<ApiMessage>(
            `${this.matchesUrl}/${matchId}`
        )
    }
}