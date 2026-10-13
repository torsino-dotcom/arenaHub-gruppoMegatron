import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../../auth/auth.service';

@Component({
  imports: [RouterLink, RouterLinkActive],
  selector: 'app-header',
  standalone: true,
  styleUrl: './header.component.scss',
  templateUrl: './header.component.html',
})
export class HeaderComponent {

  readonly authService = inject(AuthService)
  private readonly router = inject(Router)

  readonly menuOpen = signal(false)
  userMenuOpen = signal(false)

  toggleMenu(): void {
    this.menuOpen.update(open => !open)
  }

  closeMenu(): void {
    this.menuOpen.set(false)
  }

  logout(): void {
    this.authService.logout()
    this.closeUserMenu()
    this.closeMenu()
    void this.router.navigateByUrl('/')
  }

  toggleUserMenu(): void {
    this.userMenuOpen.update(isOpen => !isOpen)
  }

  closeUserMenu(): void {
    this.userMenuOpen.set(false)
  }
}
