import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { PlatformSettingsApi, UpdatePlatformSettingsApi } from "../impostazioni.model";

@Injectable({ providedIn: 'root' })
export class ImpostazioniApiService {
    private readonly http = inject(HttpClient)
    private readonly apiUrl = '/api/impostazioni'

    get(): Observable<PlatformSettingsApi> {
        return this.http.get<PlatformSettingsApi>(this.apiUrl)
    }

    update(
        data: UpdatePlatformSettingsApi
    ): Observable<PlatformSettingsApi> {
        return this.http.patch<PlatformSettingsApi>(this.apiUrl, data)
    }

}