import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../../../enviroments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserValidationService {

  private readonly apiUrl: string;

  constructor(
    private http: HttpClient
  ) {
    this.apiUrl = `${environment.apiUrlAuth}/validation/users`
  }

  checkUsernameOrEmail(value: string, field: 'username' | 'email') {
    return this.http.get<{ exists: boolean }>(`${this.apiUrl}/unique`, { params: { field, value } });
  }
}
