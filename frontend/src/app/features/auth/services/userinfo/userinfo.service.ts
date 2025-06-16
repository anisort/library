import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../../../enviroments/environment';
import {Observable} from 'rxjs';
import {UserInfoModel} from '../../models/user-info-model';

@Injectable({
  providedIn: 'root'
})
export class UserinfoService {

  private readonly apiUrl: string;

  constructor(
    private http: HttpClient
  ) {
    this.apiUrl = `${environment.apiUrlAuth}/userinfo`
  }

  updateUsername(username: {username: string}): Observable<UserInfoModel> {
    return this.http.patch<UserInfoModel>(`${this.apiUrl}/username`, username);
  }

  updateAvatar(avatar: {avatar: string}): Observable<UserInfoModel> {
    return this.http.patch<UserInfoModel>(`${this.apiUrl}/avatar`, avatar);
  }
 }
