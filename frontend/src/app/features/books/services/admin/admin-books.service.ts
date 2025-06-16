import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../../../enviroments/environment';
import {Observable} from 'rxjs';
import {BookSingleItemModel} from '../../models/book-single-item.model';

@Injectable({
  providedIn: 'root'
})
export class AdminBooksService {

  private readonly apiUrl: string;

  constructor(
    private http: HttpClient
  ) {
    this.apiUrl = `${environment.apiUrlBooks}/admin/books`
  }

  createBook(formData: FormData): Observable<BookSingleItemModel> {
    return this.http.post<BookSingleItemModel>(this.apiUrl, formData);
  }

  updateBook(id: number, formData: FormData): Observable<BookSingleItemModel> {
    return this.http.put<BookSingleItemModel>(`${this.apiUrl}/${id}`, formData);
  }

  deleteBook(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
  }
}
