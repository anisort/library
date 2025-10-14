import {Component, Input} from '@angular/core';
import {DatePipe} from '@angular/common';
import {MarkdownComponent} from 'ngx-markdown';

@Component({
  selector: 'app-message-item',
  imports: [
    DatePipe,
    MarkdownComponent
  ],
  templateUrl: './message-item.html',
  styleUrl: './message-item.scss'
})
export class MessageItem {
  @Input() message!: Message;

  isImageFile(link: string): boolean {
    return /\.(png|jpe?g|gif|webp|bmp|svg)$/i.test(link);
  }

  getFileName(link: string): string {
    try {
      const lastPart = decodeURIComponent(link.split('/').pop() || 'file');
      const parts = lastPart.split('-');
      if (parts.length > 1) {
        const filePart = parts.find(p => p.includes('.'));
        if (filePart) return filePart;
        return parts.slice(8).join('-') || lastPart;
      }
      return lastPart;
    } catch {
      return 'file';
    }
  }

}
