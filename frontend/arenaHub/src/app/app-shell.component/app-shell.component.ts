import { Component } from '@angular/core';
import { HeaderComponent } from '../shared/components/header.component/header.component';
import { RouterOutlet } from '@angular/router';
import { FooterComponent } from '../shared/components/footer.component/footer.component';

@Component({
  imports: [HeaderComponent, RouterOutlet, FooterComponent],
  selector: 'app-app-shell.component',
  standalone: true,
  styleUrl: './app-shell.component.scss',
  templateUrl: './app-shell.component.html',
})
export class AppShellComponent {}
