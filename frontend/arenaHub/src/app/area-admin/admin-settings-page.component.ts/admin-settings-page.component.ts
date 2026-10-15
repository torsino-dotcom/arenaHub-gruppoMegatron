import { DatePipe } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { ImpostazioniApiService } from '../services/impostazioni-api.service';
import { PlatformSettingsApi, UpdatePlatformSettingsApi } from '../impostazioni.model';
import { finalize } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  imports: [DatePipe],
  selector: 'app-admin-settings-page',
  styleUrl: './admin-settings-page.component.scss',
  templateUrl: './admin-settings-page.component.html',
})
export class AdminSettingsPageComponent {
  private readonly settingsApi = inject(ImpostazioniApiService)

  readonly settings = signal<PlatformSettingsApi | null>(null)
  readonly maxTorneiAttivi = signal<number | null>(null)
  readonly loading = signal(true)
  readonly saving = signal(false)
  readonly errorMessage = signal<string | null>(null)
  readonly successMessage = signal<string | null>(null)

  readonly formIsValid = computed(() => {
    const value = this.maxTorneiAttivi()
    return value !== null && Number.isInteger(value) && value > 0
  })

  constructor() {
    this.loadSettings()
  }

  loadSettings(): void {
    this.loading.set(true)
    this.errorMessage.set(null)

    this.settingsApi.get().pipe(
      finalize(() => this.loading.set(false))
    ).subscribe({
      next: settings => {
        this.settings.set(settings)
        this.maxTorneiAttivi.set(settings.max_tornei_attivi_utente)
      },
      error: (error: unknown) => {
        this.errorMessage.set(this.apiErrorMessage(error))
      }
    })
  }

  setMaxTorneiAttivi(event: Event): void {
    const input = event.target as HTMLInputElement
    this.maxTorneiAttivi.set(input.value === '' ? null : Number(input.value))
    this.successMessage.set(null)
  }

  saveSettings(event: Event): void {
    event.preventDefault()
    this.errorMessage.set(null)
    this.successMessage.set(null)

    const value = this.maxTorneiAttivi()

    if (value === null || !Number.isInteger(value) || value <= 0) {
      this.errorMessage.set('Inserisci un numero intero maggiore di zero.')
      return
    }

    const data: UpdatePlatformSettingsApi = {
      limite_iscrizioni_default: value
    }

    this.saving.set(true)

    this.settingsApi.update(data).pipe(
      finalize(() => this.saving.set(false))
    ).subscribe({
      next: updatedSettings => {
        this.settings.set(updatedSettings)
        this.maxTorneiAttivi.set(updatedSettings.max_tornei_attivi_utente)
        this.successMessage.set('Impostazione aggiornata.')
      },
      error: (error: unknown) => {
        this.errorMessage.set(this.apiErrorMessage(error))
      }
    })
  }

  private apiErrorMessage(error: unknown): string {
    if (error instanceof HttpErrorResponse) {
      const body = error.error as
        | { errore?: string; messaggio?: string }
        | string
        | null

      if (typeof body === 'string') return body

      return body?.errore
        ?? body?.messaggio
        ?? 'Non è stato possibile completare la richiesta.'
    }

    return 'Non è stato possibile completare la richiesta.'
  }

}
