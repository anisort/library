import {Component, Input} from '@angular/core';
import {BookItemModel} from '../../models/book-item.model';
import {Router} from '@angular/router';
import {NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-search-book-item',
  imports: [
    NgOptimizedImage
  ],
  templateUrl: './search-book-item.html',
  styleUrl: './search-book-item.scss'
})
export class SearchBookItem {

  @Input() book!: BookItemModel;

  constructor(
    private router: Router
  ) {
  }

  async openBookDetails() {
    await this.router.navigate(['/book-details', this.book.id]);
  }
}
