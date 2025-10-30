import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../../../enviroments/environment';
import {PagedResponse} from '../../../../core/models/pagination-response.model';
import {BookItemModel} from '../../models/book-item.model';
import {BookSingleItemModel} from '../../models/book-single-item.model';
import {BookItemListModel} from '../../models/book-item-list.model';

@Injectable({
  providedIn: 'root'
})
export class PublicBooksService {

  private readonly apiUrl: string;

  constructor(
    private http: HttpClient
  ) {
    this.apiUrl = `${environment.apiUrlBooks}/public/books`
  }

  getAllBooks(page: number, size: number, sort: string, letter: string): Observable<PagedResponse<BookItemModel>> {
    let params = new HttpParams();
    params = params.set('page', page.toString()).set('size', size.toString()).set('sort', sort).set('letter', letter);
    return this.http.get<PagedResponse<BookItemModel>>(this.apiUrl, {params});
  }

  getTopBooks(): Observable<BookItemListModel> {
    return this.http.get<BookItemListModel>(`${this.apiUrl}/top`);
  }

  searchBooks(filter: string, searchText: string): Observable<BookItemModel[]> {
    let params = new HttpParams();
    params = params.set('filter', filter).set('searchText', searchText);
    return this.http.get<BookItemModel[]>(`${this.apiUrl}/search`, {params});
  }

  getBookById(id: number): Observable<BookSingleItemModel> {
    return this.http.get<BookSingleItemModel>(`${this.apiUrl}/${id}`);
  }
}
