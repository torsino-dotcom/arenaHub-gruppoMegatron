import { Injectable, signal } from "@angular/core"
import { EnrollmentStatus, TournamentDetail } from "./tornei.model"
import { AuthUser } from "../../auth/auth.service"

interface MockRegistration {
    tournamentId: number
    email: string
    username: string
    status: EnrollmentStatus
}

@Injectable({ providedIn: 'root' })
export class TournamentEnrollmentService {
    private readonly registrations = signal<MockRegistration[]>([])

    /**
     * Trova un torneo attivo collegato ad uno user tramite email 
     * @param tournamentId 
     * @param email user.email
     * @returns Registrazione or null
     */
    findActive(tournamentId: number, email: string): MockRegistration | null {
        return this.registrations().find(registration =>
            registration.tournamentId === tournamentId &&
            registration.email === email &&
            registration.status !== 'annullato'
        ) ?? null
    }

    /**
     * Ritorna il numero di tornei confermati
     * @param tournamentId 
     * @returns number
     */
    confirmedCount(tournamentId: number): number {
        return this.registrations().filter(registration =>
            registration.tournamentId === tournamentId &&
            registration.status === 'confermato'
        ).length
    }

    enroll(tournament: TournamentDetail, user: AuthUser): EnrollmentStatus {
        const existing = this.findActive(tournament.id_tornei, user.email)

        if (existing) {
            return existing.status
        }

        const occupied = tournament.posti_occupati + this.confirmedCount(tournament.id_tornei)

        const status: EnrollmentStatus = occupied < tournament.posti_max
            ? 'confermato'
            : 'in_attesa'

        this.registrations.update(registrations => [
            ...registrations,
            {
                tournamentId: tournament.id_tornei,
                email: user.email,
                username: user.username,
                status
            }
        ])

        return status
    }

    registrationsFor(email: string): MockRegistration[] {
        return this.registrations().filter(registration =>
            registration.email === email &&
            registration.status !== 'annullato'
        )
    }

    cancel(tournamentId: number, email: string): void {
        this.registrations.update(registrations =>
            registrations.map(registration =>
                registration.tournamentId === tournamentId &&
                registration.email === email &&
                registration.status !== 'annullato'
                ? { ...registration, status: 'annullato'}
                : registration
            )
        )
    }
}