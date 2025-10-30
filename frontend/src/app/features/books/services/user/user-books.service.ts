import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../../../../enviroments/environment';
import {Observable} from 'rxjs';
import {PagedResponse} from '../../../../core/models/pagination-response.model';
import {MyBookItemModel} from '../../models/my-book-item.model';
import {MyBookSingleItemModel} from '../../models/my-book-single-item.model';
import {AddBookStatusModel} from '../../models/add-book-status.model';

@Injectable({
  providedIn: 'root'
})
export class UserBooksService {

  private readonly apiUrl: string;

  constructor(
    private http: HttpClient
  ) {
    this.apiUrl = `${environment.apiUrlBooks}/user/books`;
  }

  getUserBooks(page: number, size: number, sort: string, bookStatus: string): Observable<PagedResponse<MyBookItemModel>> {
    let params = new HttpParams();
    params = params.set('page', page.toString()).set('size', size.toString()).set('sort', sort).set('bookStatus', bookStatus);
    return this.http.get<PagedResponse<MyBookItemModel>>(this.apiUrl, {params});
  }

  getBookStatusInUserLibrary(id: number): Observable<string | null> {
    return  this.http.get<string | null>(`${this.apiUrl}/${id}`);
  }

  addBookToUserLibrary(id: number, bookStatus: AddBookStatusModel): Observable<MyBookSingleItemModel> {
    return this.http.post<MyBookSingleItemModel>(`${this.apiUrl}/${id}`, bookStatus);
  }

  updateBookStatus(id: number, bookStatus: AddBookStatusModel): Observable<MyBookSingleItemModel> {
    return this.http.patch<MyBookSingleItemModel>(`${this.apiUrl}/${id}`, bookStatus)
  }

  removeBookFromUserLibrary(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
