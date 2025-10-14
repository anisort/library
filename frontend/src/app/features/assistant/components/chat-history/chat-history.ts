import {Component, OnDestroy, OnInit} from '@angular/core';
import {map, Observable, Subject, switchMap, takeUntil} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {MessagesService} from '../../services/messages/messages-service';
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {ChatInput} from '../../../../shared/components/chat-input/chat-input';
import {MessageItem} from '../message-item/message-item';
import {InitialPromptService} from '../../services/state/initial-prompt.service';

@Component({
  selector: 'app-chat-history',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    ChatInput,
    MessageItem
  ],
  templateUrl: './chat-history.html',
  styleUrl: './chat-history.scss'
})
export class ChatHistory implements OnInit, OnDestroy {
  chatId!: number;
  messages: Message[] = [];
  promptControl!: FormControl;
  isSending = false;
  private destroy$ = new Subject<void>();

  constructor(
    private route: ActivatedRoute,
    private messagesService: MessagesService,
    private initialPromptService: InitialPromptService,
  ) {}

  ngOnInit() {
    this.promptControl = new FormControl('', [Validators.required]);
    this.loadChat();
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadChat() {
    const nav = history.state as { initialPrompt?: string, file?: File };
    this.route.paramMap.pipe(
      takeUntil(this.destroy$),
      map(params => Number(params.get('chatId'))),
      switchMap(chatId => {
        this.chatId = chatId;
        return this.messagesService.getChatHistory(chatId)
      })
    ).subscribe({
      next: (response) => {
        this.messages = response ?? [];
        const prompt = this.initialPromptService.consumePrompt(this.chatId);
        if (prompt) {
          this.promptControl.setValue(prompt.text);
          this.onSubmit({ text: prompt.text, file: prompt.file });
          this.promptControl.reset();
        }
      },
      error: (err) => {
        console.log(err);
      }
    })
  }

  onSubmit(event: { text: string, file?: File }): void {
    if (this.promptControl.valid) {
      this.isSending = true;
      const tempId = Date.now();
      const text = event.text?.trim();
      const file = event.file;

      const optimistic: Message = {
        userPrompt: { id: -tempId, prompt: text, fileLink: file ? 'uploading...' : null, createdOn: new Date().toISOString() },
        assistantResponse: { id: -tempId, response: '...', promptTokens: 0, completionTokens: 0, totalTokens: 0, createdOn: new Date().toISOString() }
      };
      this.messages = [...this.messages, optimistic];

      let request$: Observable<Message>;

      if (file) {
        const formData = new FormData();
        if (text) formData.append('prompt', text);
        formData.append('file', file);

        request$ = this.messagesService.sendPromptWithFile(formData, this.chatId);
      } else {
        request$ = this.messagesService.sendPrompt({ prompt: text }, this.chatId);
      }

      request$.subscribe({
        next: (real) => {
          this.messages = this.messages.map(m =>
            m.userPrompt.id === -tempId ? real : m
          );
        },
        error: (err) => {
          this.isSending = false;
          console.log(err);
          this.messages = this.messages.map(m =>
            m.userPrompt.id === -tempId
              ? {
                ...m,
                assistantResponse: {
                  ...m.assistantResponse,
                  response: 'Error while processing your message.'
                }
              }
              : m
          );
        },
        complete: () => {
          this.isSending = false;
        }
      });
    }
  }

}
