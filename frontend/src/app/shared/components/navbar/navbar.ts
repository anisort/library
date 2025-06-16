import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthService} from '../../../core/services/auth/auth.service';
import {RouterLink} from '@angular/router';
import {SearchBooks} from '../../../features/books/components/search-books/search-books';
import {UserStoreService} from '../../../core/services/userstore/user-store.service';
import {Subject, takeUntil} from 'rxjs';
import {UserInfoModel} from '../../../features/auth/models/user-info-model';
import {UserMenu} from '../../../features/auth/components/user-menu/user-menu';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-navbar',
  imports: [
    RouterLink,
    SearchBooks,
    CommonModule,
    UserMenu
  ],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
  standalone: true
})
export class Navbar implements OnInit, OnDestroy {

  user: UserInfoModel | null = null;
  private destroy$ = new Subject<void>();

  constructor(
    private authService: AuthService,
    private userStore: UserStoreService
  ) {}

  ngOnInit() {
    this.userStore.user$
      .pipe(takeUntil(this.destroy$))
      .subscribe(user => this.user = user);
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  isAuthenticated() {
    return this.authService.isAuthenticated()
  }

  onLogin() {
    this.authService.login();
  }

}
