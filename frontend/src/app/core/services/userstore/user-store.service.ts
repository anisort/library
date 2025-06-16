import { Injectable } from '@angular/core';
import {environment} from '../../../../enviroments/environment';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable, tap} from 'rxjs';
import {UserInfoModel} from '../../../features/auth/models/user-info-model';

@Injectable({
  providedIn: 'root'
})
export class UserStoreService {

  private readonly apiUrl: string;
  private readonly subject: BehaviorSubject<UserInfoModel | null> = new BehaviorSubject<UserInfoModel | null>(null);
  readonly user$: Observable<UserInfoModel | null> = this.subject.asObservable();

  constructor(
    private http: HttpClient
  ) {
    this.apiUrl = `${environment.apiUrlAuth}/userinfo`;
  }

  loadProfile(): Observable<UserInfoModel> {
    return this.http.get<UserInfoModel>(this.apiUrl).pipe(tap(user => this.subject.next(user)));
  }

  update(user: UserInfoModel) {
    this.subject.next(user);
  }

  clear() {
    this.subject.next(null);
  }

}
