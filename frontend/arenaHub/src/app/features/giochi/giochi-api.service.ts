import { HttpClient, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Game, GameListParams } from "./giochi.model";
import { Observable } from "rxjs";

@Injectable({ providedIn: 'root' })
export class GiochiApiService {
    private readonly http = inject(HttpClient)
    private readonly apiUrl = '/api/giochi'

    getAll(options: GameListParams = {}): Observable<Game[]> {
        let params = new HttpParams()

        if(options.page !== undefined) {
            params = params.set('page', options.page)
        }

        if (options.limit !== undefined) {
            params = params.set('limit', options.limit)
        }

        return this.http.get<Game[]>(this.apiUrl, {params})
    }

    getById(idGiochi: number): Observable<Game> {
        return this.http.get<Game>(`${this.apiUrl}/${idGiochi}`)
    }
}