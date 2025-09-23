import {Component, OnInit} from '@angular/core';
import {ChatsService} from '../../services/chats/chats-service';
import {ActivatedRoute, Router, RouterModule} from '@angular/router';
import {CommonModule} from '@angular/common';
import {Loader} from '../../../../shared/components/loader/loader';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {ChatItem} from '../chat-item/chat-item';

@Component({
  selector: 'app-chats-layout',
  imports: [
    RouterModule,
    CommonModule,
    Loader,
    FormsModule,
    ReactiveFormsModule,
    ChatItem
  ],
  templateUrl: './chats-layout.html',
  styleUrl: './chats-layout.scss'
})
export class ChatsLayout implements OnInit{

  chats: Chat[] = [];
  isLoading = false;
  selectedMenuChatId: number | null = null;
  editingChatId: number | null = null;

  constructor(
    private chatsService: ChatsService,
    private router: Router,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit() {
    this.isLoading = true;
    this.chatsService.getChats().subscribe({
      complete: () => this.isLoading = false,
      error: () => this.isLoading = false
    });
    this.chatsService.chatsStream().subscribe(chats => {
      this.chats = chats;
      const child = this.route.snapshot.firstChild;
      const hasChild = !!child;
      if(!hasChild){
        this.onNewChat()
      }
    });
  }

  onNewChat() {
    void this.router.navigate(['/chats/initial']);
  }

  onMenuToggle(chatId: number) {
    this.selectedMenuChatId = this.selectedMenuChatId === chatId ? null : chatId;
  }

  onStartEdit(chatId: number) {
    this.editingChatId = chatId;
  }

  onRename(chat: Chat) {
    const title = chat.title.trim();
    this.chatsService.changeChatTitle({ title }, chat.id).subscribe({
      complete: () => { this.selectedMenuChatId = null; this.editingChatId = null; },
      error: () => { this.selectedMenuChatId = null; this.editingChatId = null; }
    });
  }

  onDelete(chat: Chat) {
    if (confirm(`Delete chat "${chat.title}"?`)) {
      const currentChatId = Number(this.route.snapshot.firstChild?.paramMap.get('chatId'));
      const deletingActiveChat = currentChatId === chat.id;

      this.chatsService.deleteChat(chat.id).subscribe({
        complete: () => {
          if (this.selectedMenuChatId === chat.id) this.selectedMenuChatId = null;
          if (deletingActiveChat) {
            this.onNewChat()
          }
        },
        error: () => {
          if (this.selectedMenuChatId === chat.id) this.selectedMenuChatId = null;
        }
      });
    }
  }

}
