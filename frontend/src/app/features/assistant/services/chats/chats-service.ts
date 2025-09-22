import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../../../enviroments/environment';
import {BehaviorSubject, Observable, tap} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatsService {

  private readonly apiUrl: string;
  private readonly chats$ = new BehaviorSubject<Chat[]>([]);

  constructor(
    private http: HttpClient
  ) {
    this.apiUrl = `${environment.apiUrlAssistant}/chats`;
  }

  chatsStream(): Observable<Chat[]> {
    return this.chats$.asObservable();
  }

  getChats(): Observable<Chat[]> {
    return this.http.get<Chat[]>(this.apiUrl).pipe(
      tap(chats => this.chats$.next(chats))
    );
  }

  createChat(title: { title: string }): Observable<Chat> {
    return this.http.post<Chat>(this.apiUrl, title).pipe(
      tap(newChat => {
        const current = this.chats$.getValue();
        this.chats$.next([...current, newChat]);
      })
    );
  }

  changeChatTitle(title: { title: string }, chatId: number): Observable<Chat> {
    return this.http.patch<Chat>(`${this.apiUrl}/${chatId}`, title).pipe(
      tap(updated => {
        const current = this.chats$.getValue();
        this.chats$.next(current.map(chat => chat.id === chatId ? updated : chat));
      })
    );
  }

  deleteChat(chatId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${chatId}`).pipe(
      tap(() => {
        const current = this.chats$.getValue();
        this.chats$.next(current.filter(chat => chat.id !== chatId));
      })
    );
  }
}
