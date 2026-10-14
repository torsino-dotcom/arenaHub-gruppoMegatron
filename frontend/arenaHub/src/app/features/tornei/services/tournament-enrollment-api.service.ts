import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { AdminEnrollmentStatus, ApiMessage, EnrollmentApiStatus, MyTournamentEnrollmentApi, TournamentEnrollmentApi, TournamentEnrollmentsResponse } from "../tornei.model";
import { HttpClient, HttpParams } from "@angular/common/http";

@Injectable({ providedIn: 'root' })
export class TournamentEnrollmentApiService {

    private readonly http = inject(HttpClient)
    private readonly tournamentsUrl = '/api/tornei'
    private readonly usersUrl = '/api/utenti'

    getMine(): Observable<MyTournamentEnrollmentApi[]> {
        return this.http.get<MyTournamentEnrollmentApi[]>(
            `${this.usersUrl}/me/iscrizioni`
        )
    }

    getForTournament(
        tournamentId: number,
        status?: EnrollmentApiStatus
    ): Observable<TournamentEnrollmentsResponse> {
        let params = new HttpParams()

        if (status) {
            params = params.set('stato', status)
        }

        return this.http.get<TournamentEnrollmentsResponse>(
            `${this.tournamentsUrl}/${tournamentId}/iscrizioni`,
            { params }
        )
    }

    enroll(tournamentId: number): Observable<TournamentEnrollmentApi> {
        return this.http.post<TournamentEnrollmentApi>(
            `${this.tournamentsUrl}/${tournamentId}/iscrizioni`,
            null
        )
    }

    cancelMine(tournamentId: number): Observable<ApiMessage> {
        return this.http.delete<ApiMessage>(
            `${this.tournamentsUrl}/${tournamentId}/iscrizioni`
        )
    }

    updateStatus(
        tournamentId: number,
        enrollmentId: number,
        status: AdminEnrollmentStatus
    ): Observable<TournamentEnrollmentApi> {
        return this.http.patch<TournamentEnrollmentApi>(
            `${this.tournamentsUrl}/${tournamentId}/iscrizioni/${enrollmentId}`,
            { stato: status }
        )
    }

    remove(
        tournamentId: number,
        enrollmentId: number
    ): Observable<ApiMessage> {
        return this.http.delete<ApiMessage>(
            `${this.tournamentsUrl}/${tournamentId}/iscrizioni/${enrollmentId}`
        )
    }


}