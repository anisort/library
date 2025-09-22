import {Component, OnInit} from '@angular/core';
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {ChatsService} from '../../services/chats/chats-service';
import {ActivatedRoute, Router} from '@angular/router';
import {ChatInput} from '../../../../shared/components/chat-input/chat-input';

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
    private route: ActivatedRoute
  ) {
  }

  ngOnInit() {
    this.promptControl = new FormControl('', [Validators.required]);
  }

  onSubmit() {
    if (this.promptControl.valid) {
      const text = this.promptControl.value?.trim();
      this.chatsService.createChat({ title: text.slice(0, 40) || 'New chat' }).subscribe({
        next: async (chat) => {
          await this.router.navigate(['/chats', chat.id], {
            relativeTo: this.route,
            state: { initialPrompt: text }
          });
        }
      });
    }
  }
}
