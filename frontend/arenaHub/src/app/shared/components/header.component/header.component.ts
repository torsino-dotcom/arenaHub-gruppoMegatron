import { Component, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  imports: [RouterLink, RouterLinkActive],
  selector: 'app-header',
  standalone: true,
  styleUrl: './header.component.scss',
  templateUrl: './header.component.html',
})
export class HeaderComponent {
  readonly menuOpen = signal(false)

  toggleMenu(): void {
    this.menuOpen.update(open => !open)
  }

  closeMenu(): void {
    this.menuOpen.set(false)
  }
}
