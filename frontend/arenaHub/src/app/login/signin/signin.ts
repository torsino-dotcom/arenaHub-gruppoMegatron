import { Component, model, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './signin.html',
  styleUrls: ['./signin.scss']
})
export class Signin {
  
  email = model<string>('');
  password = model<string>('');

  
  isLoading = signal<boolean>(false);
  errorMessage = signal<string | null>(null);

  
  isFormValid = computed(() => {
    const emailVal = this.email().trim();
    const passwordVal = this.password();
    return emailVal.length > 0 && passwordVal.length >= 6;
  });

  onSubmit(event: Event) {
    event.preventDefault();

    if (!this.isFormValid()) {
      this.errorMessage.set('Inserisci una email valida e una password di almeno 6 caratteri.');
      return;
    }

    this.errorMessage.set(null);
    this.isLoading.set(true);

    console.log('Login con:', {
      email: this.email(),
      password: this.password()
    });

    
    setTimeout(() => {
      this.isLoading.set(false);
    }, 1500);
  }
}