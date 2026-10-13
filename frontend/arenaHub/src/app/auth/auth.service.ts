import { computed, Injectable, signal } from "@angular/core"

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
    private readonly accounts = signal<MockAccount[]>([
        {
            username: 'ArenaTester',
            email: 'tester@arenahub.it',
            password: 'arena123',
            ruolo: 'utente'
        },
        {
            username: 'ArenaAdmin',
            email: 'admin@arenahub.it',
            password: 'admin123',
            ruolo: 'admin',
        }
    ])

    private readonly user = signal<AuthUser | null>(null)

    readonly currentUser = this.user.asReadonly()
    readonly isAuthenticated = computed(() => this.user() !== null)

    async login(email: string, password: string): Promise<void> {
        await this.simulateRequest()

        const normalizedEmail = email.trim().toLocaleLowerCase()
        const account = this.accounts().find(
            user => user.email.toLocaleLowerCase() === normalizedEmail &&
                user.password === password
        )

        if (!account) {
            throw new Error('Email o password non corretti.')
        }

        this.user.set({
            username: account.username,
            email: account.email,
            ruolo: account.ruolo
        })
    }

    async register(username: string, email: string, password: string): Promise<void> {
        await this.simulateRequest()

        const normalizedEmail = email.trim().toLocaleLowerCase()
        const emailExists = this.accounts().some(
            account => account.email.toLowerCase() === normalizedEmail
        )

        if (emailExists) {
            throw new Error('Esiste già un account con questa email.')
        }

        const account: MockAccount = {
            username: username.trim(),
            email: normalizedEmail,
            ruolo: 'utente',
            password
        }

        this.accounts.update(accounts => [...accounts, account])
        this.user.set({
            username: account.username,
            email: account.email,
            ruolo: account.ruolo
        })
    }

    logout(): void {
        this.user.set(null)
    }

    private simulateRequest(): Promise<void> {
        return new Promise(resolve => setTimeout(resolve, 500))
    }
}