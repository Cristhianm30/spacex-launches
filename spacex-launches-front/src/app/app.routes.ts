import { Routes } from '@angular/router';
import { Notfound } from './shared/notfound/notfound';
import { Shell } from './components/layout/shell/shell';

export const routes: Routes = [
  {
    path: '',
    component: Shell,
    children: [
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/dashboard/dashboard').then(m => m.Dashboard)
      },
      { 
        path: '',
        pathMatch: 'full',
        redirectTo: 'dashboard' 
      }
    ]
  },
  {
    path: '**',
    component: Notfound
  }
];