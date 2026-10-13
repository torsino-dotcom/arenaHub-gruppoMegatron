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
import { Settings} from './area-utenti/settings/settings';
import { AreaAdmin } from './area-admin/area-admin/area-admin';
import { adminGuard } from './auth/guard/admin.guard';

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
            path: 'admin', canActivate: [adminGuard], component: AreaAdmin
          },
          {
            path: 'tornei', component: MyTournaments
          },
          {
            path: 'impostazioni', component: Settings
          },
        ],
      }
    ],
  },
  {
    path: '**',
    redirectTo: '',
  },
];
