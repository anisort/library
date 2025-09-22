import {Component, OnDestroy, OnInit} from '@angular/core';
import {map, Subject, switchMap, takeUntil} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {MessagesService} from '../../services/messages/messages-service';
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {ChatInput} from '../../../../shared/components/chat-input/chat-input';
import {MessageItem} from '../message-item/message-item';

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
    private messagesService: MessagesService
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
    const nav = history.state as { initialPrompt?: string };
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
        if (nav?.initialPrompt) {
          this.promptControl.setValue(nav.initialPrompt);
          this.onSubmit();
          history.replaceState({}, '');
        }
      },
      error: (err) => {
        console.log(err);
      }
    })
  }

  onSubmit(): void {
    if (this.promptControl.valid) {
      this.isSending = true;
      const tempId = Date.now();
      const text = this.promptControl.value.trim();
      this.promptControl.reset();
      const optimistic: Message = {
        userPrompt: { id: -tempId, prompt: text, createdOn: new Date().toISOString() },
        assistantResponse: { id: -tempId, response: '...', promptTokens: 0, completionTokens: 0, totalTokens: 0, createdOn: new Date().toISOString() }
      };
      this.messages = [...this.messages, optimistic];
      this.messagesService.sendPrompt({ prompt: text }, this.chatId).subscribe({
        next: (real) => {
          this.messages = this.messages.map(message =>
            message.userPrompt.id === optimistic.userPrompt.id ? real : message
          );
        },
        error: (err) => {
          this.messages = this.messages.filter(message => message.userPrompt.id !== optimistic.userPrompt.id);
          this.isSending = false;
          console.log(err);
        },
        complete: () => {
          this.isSending = false;
        }
      })
    }
  }
}
