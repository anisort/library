import { Injectable } from '@angular/core';
import {
  Router,
  UrlTree,
  CanMatch,
  Route, UrlSegment
} from '@angular/router';
import { AuthService } from '../services/auth/auth.service';

@Injectable({ providedIn: 'root' })
export class UnauthGuard implements CanMatch {
  constructor(private authService: AuthService, private router: Router) {}

  canMatch(
    _route: Route,
    _segments: UrlSegment[]
  ): boolean | UrlTree {
    return this.authService.isAuthenticated()
      ? this.router.createUrlTree(['/home'])
      : true;
  }
}
