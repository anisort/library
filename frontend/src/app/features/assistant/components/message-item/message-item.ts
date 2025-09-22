import {Component, Input} from '@angular/core';
import {DatePipe} from '@angular/common';
import {MarkdownComponent} from 'ngx-markdown';

@Component({
  selector: 'app-message-item',
  imports: [
    DatePipe,
    MarkdownComponent,
  ],
  templateUrl: './message-item.html',
  styleUrl: './message-item.scss'
})
export class MessageItem {
  @Input() message!: Message;
}
