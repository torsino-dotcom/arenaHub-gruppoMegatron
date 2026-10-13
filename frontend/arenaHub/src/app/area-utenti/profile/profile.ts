import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  imports: [CommonModule],
  standalone: true,
  selector: 'app-profile',
  styleUrl: './profile.scss',
  templateUrl: './profile.html',
})
export class Profile {
  username = signal<string>('nome_utente');
  email = signal<string>('nome.cognome@example.com');
}
