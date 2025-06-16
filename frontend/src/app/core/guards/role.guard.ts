import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router, UrlTree } from '@angular/router';
import { AuthService } from '../services/auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean | UrlTree {

    const isAuthenticated = this.authService.isAuthenticated();

    if (!isAuthenticated) {
      this.authService.login();
      return false;
    }

    const expectedRoles = route.data['roles'] as string[];
    const userRole = this.authService.role;

    if (expectedRoles?.length && !expectedRoles.includes(userRole)) {
      return this.router.parseUrl('/access-denied');
    }

    return true;
  }

}
