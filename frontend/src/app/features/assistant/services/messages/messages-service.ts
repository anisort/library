import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../../../enviroments/environment';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MessagesService {

  private readonly apiUrl: string;

  constructor(
    private http: HttpClient
  ) {
    this.apiUrl = `${environment.apiUrlAssistant}/messages`;
  }

  sendPrompt(prompt: { prompt: string }, chatId: number): Observable<Message> {
    return this.http.post<Message>(`${this.apiUrl}/${chatId}`, prompt);
  }

  sendPromptWithFile(formData: FormData, chatId: number): Observable<Message> {
    return this.http.post<Message>(`${this.apiUrl}/file/${chatId}`, formData);
  }

  getChatHistory(chatId: number): Observable<Message[]> {
    return this.http.get<Message[]>(`${this.apiUrl}/${chatId}`);
  }
}
