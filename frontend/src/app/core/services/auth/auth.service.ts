import { Injectable } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { authConfig } from '../../config/auth-config';
import {UserStoreService} from '../userstore/user-store.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(
    private oauthService: OAuthService,
    private userStore: UserStoreService
  ) {
  }

  async init() {
    this.oauthService.setStorage(localStorage);
    this.oauthService.configure(authConfig);
    await this.oauthService.loadDiscoveryDocumentAndTryLogin();
    if (this.oauthService.hasValidAccessToken()) {
      this.userStore.loadProfile().subscribe();
    }
  }

  login() {
    this.oauthService.initLoginFlow();
  }

  logout() {
    this.oauthService.logOut();
    this.userStore.clear();
  }

  isAuthenticated() {
    return !!this.oauthService.getAccessToken();
  }

  get role() {
    const claims: any = this.oauthService.getIdentityClaims();
    return claims ? claims['role'] : null;
  }

  get token() {
    return this.oauthService.getAccessToken();
  }
}
