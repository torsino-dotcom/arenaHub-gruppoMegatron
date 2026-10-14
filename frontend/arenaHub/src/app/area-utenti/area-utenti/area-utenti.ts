import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../auth/auth.service';

@Component({
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  standalone: true,
  selector: 'app-area-utenti',
  styleUrl: './area-utenti.scss',
  templateUrl: './area-utenti.html',
})
export class AreaUtenti {
  readonly authService = inject(AuthService)
}
