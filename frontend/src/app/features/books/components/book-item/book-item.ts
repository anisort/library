import {Component, EventEmitter, Input, Output} from '@angular/core';
import {BookItemModel} from '../../models/book-item.model';
import {Router} from '@angular/router';
import {NgClass, NgOptimizedImage} from '@angular/common';
import { BookStatusUtils } from '../../utils/book-status.utils';
import {AuthService} from '../../../../core/services/auth/auth.service';

@Component({
  selector: 'app-book-item',
  imports: [
    NgOptimizedImage,
    NgClass
  ],
  templateUrl: './book-item.html',
  styleUrl: './book-item.scss',
  standalone: true
})
export class BookItem {

  @Input() book!: BookItemModel;
  @Input() bookStatus?: string;
  @Output() bookDeleted = new EventEmitter<number>();

  constructor(
    private router: Router,
    public authService: AuthService
  ) {
  }

  async openBookDetails() {
    await this.router.navigate(['/book-details', this.book.id]);
  }

  async onUpdate() {
    await this.router.navigate(['/book-form', this.book.id]);
  }

  onDelete() {
    if(confirm('Are you sure you want to delete this book?')){
      this.bookDeleted.emit(this.book.id);
    }
  }

  getStatusClasses() {
    return BookStatusUtils.getStatusClasses(this.bookStatus ?? '');
  }

  formatStatus(): string {
    return BookStatusUtils.formatBookStatus(this.bookStatus ?? '');
  }
}
