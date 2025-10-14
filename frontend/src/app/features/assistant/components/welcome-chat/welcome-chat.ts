import {Component, OnInit} from '@angular/core';
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {ChatsService} from '../../services/chats/chats-service';
import {ActivatedRoute, Router} from '@angular/router';
import {ChatInput} from '../../../../shared/components/chat-input/chat-input';
import {InitialPromptService} from '../../services/state/initial-prompt.service';

@Component({
  selector: 'app-welcome-chat',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    ChatInput
  ],
  templateUrl: './welcome-chat.html',
  styleUrl: './welcome-chat.scss'
})
export class WelcomeChat implements OnInit {
  promptControl!: FormControl;

  constructor(
    private chatsService: ChatsService,
    private router: Router,
    private route: ActivatedRoute,
    private initialPromptService: InitialPromptService,
  ) {}

  ngOnInit() {
    this.promptControl = new FormControl('', [Validators.required]);
  }

  onSubmit(event: { text: string; file?: File }) {
    const text = event.text?.trim();
    const file = event.file;

    this.chatsService.createChat({ title: text.slice(0, 40) || 'New chat' }).subscribe({
      next: async (chat) => {
        this.initialPromptService.setPrompt({ text, file, chatId: chat.id });
        await this.router.navigate(['/chats', chat.id], {
          relativeTo: this.route
        });
      }
    });
  }
}
