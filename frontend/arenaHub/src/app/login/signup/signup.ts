import { Component, model, computed, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../auth/auth.service';


@Component({
  imports: [CommonModule, FormsModule, RouterLink],
  selector: 'app-signup',
  standalone: true,
  styleUrl: './signup.scss',
  templateUrl: './signup.html',
})
export class Signup {

  private readonly authService = inject(AuthService)
  private readonly router = inject(Router)

  username = signal('')
  email = signal('')
  password = signal('')
  confirmPassword = signal('')

  isLoading = signal(false)
  errorMessage = signal<string | null>(null);

  isFormValid = computed(() =>
    this.username().trim().length > 0 &&
    this.email().trim().length > 0 &&
    this.password().length >= 6 &&
    this.password() === this.confirmPassword()
  )

  async onSubmit(event: Event): Promise<void> {
    event.preventDefault();

    if (this.password() !== this.confirmPassword()) {
      this.errorMessage.set('Password diverse')
      return;
    }

    if(!this.isFormValid()) {
      this.errorMessage.set('Controlla i dati inseriti e riprova.')
      return
    }

    this.errorMessage.set(null)
    this.isLoading.set(true);

    try {
      await this.authService.register(
        this.username(),
        this.email(),
        this.password()
      )
      await this.router.navigateByUrl('/')
    } catch (error) {
      error instanceof Error ? error.message : 'Si è verificato un errore.'
    } finally {
      this.isLoading.set(false)
    }

    console.log('Registrazione con:', {
      username: this.username(),
      email: this.email(),
      password: this.password()
    });

    setTimeout(() => {
      this.isLoading.set(false);
    }, 1500);
  }
}
