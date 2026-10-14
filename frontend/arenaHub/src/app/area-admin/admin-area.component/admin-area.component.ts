import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  imports: [RouterOutlet, RouterLinkActive, RouterLink],
  selector: 'app-admin-area.component',
  styleUrl: './admin-area.component.scss',
  templateUrl: './admin-area.component.html',
})
export class AdminAreaComponent {}
