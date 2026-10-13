import { Routes } from '@angular/router';
import { AppShellComponent } from './app-shell.component/app-shell.component';
import { HomePageComponent } from './features/home-page.component/home-page.component';
import { Signin } from './login/signin/signin';
import { Signup } from './login/signup/signup';

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
      }
    ],
  },
  {
    path: '**',
    redirectTo: '',
  },
];
