import { computed, inject, Injectable, signal } from "@angular/core"
import { AuthApiService } from "./auth-api.service"
import { LoginApiRequest, RegisterApiRequest } from "./auth.model"
import { firstValueFrom } from "rxjs"
import { HttpErrorResponse } from "@angular/common/http"

export type UserRole = 'utente' | 'admin'

export interface AuthUser {
    username: string
    email: string
    ruolo: UserRole
}
/**
 * interfaccia per provare i dati
 */
interface MockAccount extends AuthUser {
    password: string
}

@Injectable({ providedIn: 'root' })
export class AuthService {
    private readonly authApi = inject(AuthApiService)
    private readonly user = signal<AuthUser | null>(null)
    // private readonly accounts = signal<MockAccount[]>([
    //     {
    //         username: 'ArenaTester',
    //         email: 'tester@arenahub.it',
    //         password: 'arena123',
    //         ruolo: 'utente'
    //     },
    //     {
    //         username: 'ArenaAdmin',
    //         email: 'admin@arenahub.it',
    //         password: 'admin123',
    //         ruolo: 'admin',
    //     }
    // ])


    readonly currentUser = this.user.asReadonly()
    readonly isAuthenticated = computed(() => this.user() !== null)

    async login(data: LoginApiRequest): Promise<void> {
        try {
            const user = await firstValueFrom(this.authApi.login(data))
            this.user.set(user)
        } catch (error) {
            throw this.toAuthError(error, 'Accesso non riuscito.')
        }
    }

    async register(data: RegisterApiRequest): Promise<void> {
        try {
            const user = await firstValueFrom(this.authApi.register(data))
            this.user.set(user)
        } catch (error) {
            throw this.toAuthError(error, 'Registrazione non riuscita.')
        }
    }

    private toAuthError(error: unknown, fallback: string): Error {
        if (error instanceof HttpErrorResponse) {
            const body = error.error as
                | { messaggio?: unknown; message?: unknown; error?: unknown }
                | string
                | null

            if (typeof body === 'string') {
                return new Error(body)
            }

            const message = body?.messaggio ?? body?.message ?? body?.error

            if (typeof message === 'string') {
                return new Error(message)
            }
        }

        return error instanceof Error ? error : new Error(fallback)
    }


    logout(): void {
        this.user.set(null)
        void firstValueFrom(this.authApi.logout()).catch(() => undefined)
    }

    async restoreSession(): Promise<void> {
        try {
            const user = await firstValueFrom(this.authApi.getMe())
            this.user.set(user)
        } catch {
            this.user.set(null)
        }
    }


}