import { AuthConfig } from 'angular-oauth2-oidc';
import {environment} from '../../../enviroments/environment';

export const authConfig: AuthConfig = {
  issuer: environment.oauthIssuer,
  redirectUri: window.location.origin + '/home',
  clientId: environment.oauthClientId,
  dummyClientSecret: environment.oauthSecret,
  responseType: 'code',
  scope: 'openid profile',
  showDebugInformation: true,
  requireHttps: false,
  clearHashAfterLogin: true
};
