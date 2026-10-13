import { Component, model, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';


@Component({
  imports: [ CommonModule, FormsModule, RouterLink],
  selector: 'app-signup',
  standalone: true,
  styleUrl: './signup.scss',
  templateUrl: './signup.html',
})
export class Signup {
  username = model<string>('')
  email = model<string>('')
  password = model<string>('')
  confirmPassword = model<string>('');

  isLoading = signal<boolean>(false)
  errorMessage = signal<string | null>(null);

  isFormValid = computed(() => {
    const user = this.username().trim()
    const mail = this.email().trim()
    const pass = this.password()
    const confirm = this.confirmPassword();

    return (
      user.length > 0 &&
      mail.length > 0 &&
      pass.length >= 6 &&
      pass === confirm
    );
  });

  onSubmit(event: Event) {
    event.preventDefault();

    if (this.password() !== this.confirmPassword()) {
      this.errorMessage.set('Password diverse')
      return;
    }

    this.errorMessage.set(null)
    this.isLoading.set(true);

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
