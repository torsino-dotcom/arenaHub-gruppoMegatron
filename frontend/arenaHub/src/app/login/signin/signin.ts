import { Component, model, computed, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './signin.html',
  styleUrls: ['./signin.scss']
})
export class Signin {

  //services
  private readonly authService = inject(AuthService)
  private readonly router = inject(Router)

  email = signal('');
  password = signal('');
  
  isLoading = signal(false);
  errorMessage = signal<string | null>(null);

  isFormValid = computed(() => 
    this.email().trim().length > 0 && this.password().length >= 6
  )

  async onSubmit(event: Event): Promise<void> {
    event.preventDefault();

    if (!this.isFormValid()) {
      this.errorMessage.set('Inserisci una email valida e una password di almeno 6 caratteri.');
      return;
    }

    this.errorMessage.set(null);
    this.isLoading.set(true);

    try {
      await this.authService.login(this.email(), this.password())
      await this.router.navigateByUrl('/')
    } catch (error) {
      this.errorMessage.set(
        error instanceof Error ? error.message : 'Si è verificato un errore.'
      )
    } finally {
      this.isLoading.set(false)
    }
  }
}