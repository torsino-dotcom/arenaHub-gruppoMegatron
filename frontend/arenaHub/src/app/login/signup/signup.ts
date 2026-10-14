import { Component, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './signup.html',
  styleUrl: './signup.scss',
})
export class Signup {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  username = signal('');
  name = signal('');
  surname = signal('');
  email = signal('');
  password = signal('');
  confirmPassword = signal('');

  isLoading = signal(false);
  errorMessage = signal<string | null>(null);

  isFormValid = computed(() =>
    this.username().trim().length > 0 &&
    this.name().trim().length > 0 &&
    this.surname().trim().length > 0 &&
    this.email().trim().length > 0 &&
    this.password().length >= 6 &&
    this.password() === this.confirmPassword()
  );

  async onSubmit(event: Event): Promise<void> {
    event.preventDefault();

    if (this.password() !== this.confirmPassword()) {
      this.errorMessage.set('Password diverse');
      return;
    }

    if (!this.isFormValid()) {
      this.errorMessage.set('Controlla i dati inseriti e riprova.');
      return;
    }

    this.errorMessage.set(null);
    this.isLoading.set(true);

    try {
      await this.authService.register({
        nome: this.name().trim(),
        cognome: this.surname().trim(),
        username: this.username().trim(),
        email: this.email().trim(),
        password: this.password()
      })
      await this.router.navigateByUrl('/');
    } catch (error) {
      this.errorMessage.set(
        error instanceof Error ? error.message : 'Si è verificato un errore.'
      );
    } finally {
      this.isLoading.set(false);
    }
  }
}