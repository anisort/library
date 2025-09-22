import { Routes } from '@angular/router';

export const CHATS_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('../components/chats-layout/chats-layout').then(c => c.ChatsLayout),
    children: [
      {
        path: 'initial',
        loadComponent: () =>
          import('../components/welcome-chat/welcome-chat').then(c => c.WelcomeChat),
      },
      {
        path: ':chatId',
        loadComponent: () =>
          import('../components/chat-history/chat-history').then(c => c.ChatHistory),
      },
    ],
  },
];
