import { Component, input, output } from '@angular/core';

@Component({
  imports: [],
  selector: 'app-home-hero',
  standalone: true,
  styleUrl: './home-hero.component.scss',
  templateUrl: './home-hero.component.html',
})
export class HomeHeroComponent {
  eyebrow = input("ARENAHUB")
  title = input('Arenahub')
  subtitle = input('Scopri i tornei, trova la tua community e preparati a giocare.')
  ctaLabel = input('Scopri i tornei')

  ctaClick = output<void>()
}
