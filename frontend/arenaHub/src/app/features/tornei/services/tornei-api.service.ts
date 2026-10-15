import { HttpClient, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { ApiMessage, NewTournamentApi, TournamentApiDetail, TournamentApiFilters, TournamentApiItem, TournamentPatchApi } from "../tornei.model";
import { Observable } from "rxjs";

@Injectable({ providedIn: 'root' })
export class TorneiApiService {
    private readonly http = inject(HttpClient)
    private readonly apiUrl = '/api/tornei'

    getAll(filters: TournamentApiFilters = {}): Observable<TournamentApiItem[]> {
        console.log("passo");
        let params = new HttpParams()

        for (const [key, value] of Object.entries(filters)) {
            if (value !== undefined && value !== null && value !== '') {
                params = params.set(key, String(value))
            }
        }

        return this.http.get<TournamentApiItem[]>(this.apiUrl, { params })
    }

    getById(idTornei: number): Observable<TournamentApiDetail> {
        return this.http.get<TournamentApiDetail>(`${this.apiUrl}/${idTornei}`)
    }

    create(data: NewTournamentApi): Observable<TournamentApiDetail> {
        return this.http.post<TournamentApiDetail>(this.apiUrl, data)
    }

    update(
        idTornei: number,
        data: TournamentPatchApi
    ): Observable<TournamentApiDetail> {
        return this.http.patch<TournamentApiDetail>(
            `${this.apiUrl}/${idTornei}`,
            data
        )
    }

    delete(idTornei: number): Observable<ApiMessage> {
        return this.http.delete<ApiMessage>(`${this.apiUrl}/${idTornei}`)
    }
}