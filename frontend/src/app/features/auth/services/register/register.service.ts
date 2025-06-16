import { Injectable } from '@angular/core';
import {RegisterUserModel} from '../../models/register-user.model';
import {UserInfoModel} from '../../models/user-info-model';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../../../enviroments/environment';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  private readonly apiUrl: string;

  constructor(
    private http: HttpClient
  ) {
    this.apiUrl = `${environment.apiUrlAuth}/auth/register`
  }

  register(registerUser: RegisterUserModel) : Observable<UserInfoModel> {
    return this.http.post<UserInfoModel>(this.apiUrl, registerUser);
  }
}
