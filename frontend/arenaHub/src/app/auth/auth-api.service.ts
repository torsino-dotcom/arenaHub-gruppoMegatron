import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { AuthUserApi, LoginApiRequest, LogoutApiResponse, RegisterApiRequest } from "./auth.model";
import { Observable } from "rxjs";

@Injectable({ providedIn: 'root' })
export class AuthApiService {
    private readonly http = inject(HttpClient)
    private readonly apiUrl = '/api/auth'

    login(data: LoginApiRequest): Observable<AuthUserApi> {
        return this.http.post<AuthUserApi>(`${this.apiUrl}/login`, data)
    }

    register(data: RegisterApiRequest): Observable<AuthUserApi> {
        return this.http.post<AuthUserApi>(`${this.apiUrl}/register`, data)
    }

    logout(): Observable<LogoutApiResponse> {
    return this.http.post<LogoutApiResponse>(
      `${this.apiUrl}/logout`,
      {}
    )
  }
}