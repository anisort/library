import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InitialPromptService {
  private initialPrompt$ = new BehaviorSubject<{text: string, file?: File, chatId: number} | null>(null);

  setPrompt(prompt: {text: string, file?: File, chatId: number}) {
    this.initialPrompt$.next(prompt);
  }

  consumePrompt(chatId: number) {
    const prompt = this.initialPrompt$.value;
    if (prompt?.chatId === chatId) {
      this.initialPrompt$.next(null);
      return prompt;
    }
    return null;
  }
}
