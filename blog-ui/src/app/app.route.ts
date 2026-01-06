import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/search',
    pathMatch: 'full'
  },
  {
    path: 'search',
    loadComponent: () => import('./components/category-search/category-search.component')
      .then(m => m.CategorySearchComponent)
  }
];
