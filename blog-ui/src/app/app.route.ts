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
  },
  {
    path: 'blog/:id',
    loadComponent: () => import('./components/blog-detail/blog-detail.component')
      .then(m => m.BlogDetailComponent)
  }
];
