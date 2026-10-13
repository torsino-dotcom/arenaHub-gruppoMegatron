import { Routes } from '@angular/router';
import { AppShellComponent } from './app-shell.component/app-shell.component';
import { HomePageComponent } from './features/home-page.component/home-page.component';
import { TournamentListPageComponent } from './features/tornei/tournament-list-page.component/tournament-list-page.component';
import { TournamentDetailPageComponent } from './features/tornei/tournament-detail-page.component/tournament-detail-page.component';

export const routes: Routes = [
  {
    path: '',
    component: AppShellComponent,
    children: [
      {
        path: '',
        component: HomePageComponent,
      },
      {
        path: 'tornei',
        component: TournamentListPageComponent
      },
      {
        path: 'tornei/:id',
        component: TournamentDetailPageComponent,
      }
    ],
  },
  {
    path: '**',
    redirectTo: '',
  },
];
