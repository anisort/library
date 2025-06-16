import { Routes } from '@angular/router';
import {RoleGuard} from './core/guards/role.guard';
import {UnauthGuard} from './core/guards/unauth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path: 'home',
    loadComponent: () => import('./features/books/components/top-book-list/top-book-list')
      .then(c => c.TopBookList)
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/components/register/register')
      .then(c => c.Register),
    canMatch: [UnauthGuard]
  },
  {
    path: 'profile',
    loadComponent: () => import('./features/auth/components/user-profile/user-profile')
      .then(c => c.UserProfile),
    canActivate: [RoleGuard],
    data: { roles: ['ADMIN', 'USER'] }
  },
  {
    path: 'catalog',
    loadComponent: () => import('./features/books/components/book-list/book-list')
      .then(c => c.BookList)
  },
  {
    path: 'my-library',
    loadComponent: () => import('./features/books/components/my-books-list/my-books-list')
      .then(c => c.MyBooksList),
    canActivate: [RoleGuard],
    data: { roles: ['ADMIN', 'USER'] }
  },
  {
    path: 'book-details/:id',
    loadComponent: () => import('./features/books/components/book-details/book-details')
      .then(c => c.BookDetails)
  },
  {
    path: 'book-form',
    loadComponent: () => import('./features/books/components/book-form/book-form')
      .then(c => c.BookForm),
    canActivate: [RoleGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'book-form/:id',
    loadComponent: () => import('./features/books/components/book-form/book-form')
      .then(c => c.BookForm),
    canActivate: [RoleGuard],
    data: { roles: ['ADMIN'] }
  },

  {
    path: 'access-denied',
    loadComponent: () => import('./core/components/forbidden/forbidden')
      .then(c => c.Forbidden)
  },
  {
    path: '**',
    loadComponent: () => import('./core/components/not-found/not-found')
      .then(c => c.NotFound)
  }
];
