import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  standalone: true,
  selector: 'app-area-utenti',
  styleUrl: './area-utenti.scss',
  templateUrl: './area-utenti.html',
})
export class AreaUtenti {}
