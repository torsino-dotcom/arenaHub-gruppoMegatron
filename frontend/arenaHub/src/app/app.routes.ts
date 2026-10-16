import { Routes } from '@angular/router';
import { AppShellComponent } from './app-shell.component/app-shell.component';
import { HomePageComponent } from './features/home-page.component/home-page.component';
import { Signin } from './login/signin/signin';
import { Signup } from './login/signup/signup';
import { TournamentListPageComponent } from './features/tornei/tournament-list-page.component/tournament-list-page.component';
import { TournamentDetailPageComponent } from './features/tornei/tournament-detail-page.component/tournament-detail-page.component';
import { AreaUtenti } from './area-utenti/area-utenti/area-utenti';
import { Profile } from './area-utenti/profile/profile';
import { MyTournaments } from './area-utenti/my-tournaments/my-tournaments';
import { Settings } from './area-utenti/settings/settings';
import { adminGuard } from './auth/guard/admin.guard';
import { AdminAreaComponent } from './area-admin/admin-area.component/admin-area.component';
import { AdminTournamentsPageComponent } from './area-admin/admin-tournaments-page.component/admin-tournaments-page.component';
import { AdminSettingsPageComponent } from './area-admin/admin-settings-page.component.ts/admin-settings-page.component';

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
        path: 'login',
        component: Signin,
      },
      {
        path: 'registrati',
        component: Signup,
      },
      {
        path: 'tornei',
        component: TournamentListPageComponent
      },
      {
        path: 'tornei/:id',
        component: TournamentDetailPageComponent,
      },
      {
        path: 'area-utenti',
        component: AreaUtenti,
        children: [
          {
            path: '', redirectTo: 'profilo', pathMatch: 'full'
          },
          {
            path: 'profilo', component: Profile
          },
          {
            path: 'tornei', component: MyTournaments
          },
          {
            path: 'impostazioni', component: Settings
          },
        ],
      },
      {
        path: 'admin',
        component: AdminAreaComponent,
        canActivate: [adminGuard],
        children: [
          { 
            path: '', pathMatch: 'full', redirectTo: 'tornei' 
          },
          { 
            path: 'tornei', component: AdminTournamentsPageComponent 
          },
          { 
            path: 'impostazioni', component: AdminSettingsPageComponent
          },
        ],
      },
    ],
  },
  {
    path: '**',
    redirectTo: '',
  },
];
