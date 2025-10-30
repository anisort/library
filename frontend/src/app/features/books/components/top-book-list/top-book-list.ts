import {Component, OnInit} from '@angular/core';
import {BookItemModel} from '../../models/book-item.model';
import {PublicBooksService} from '../../services/public/public-books.service';
import { deleteBookWithReload } from '../../utils/delete-book-with-reload';
import {AdminBooksService} from '../../services/admin/admin-books.service';
import {Loader} from '../../../../shared/components/loader/loader';
import {BookItem} from '../book-item/book-item';

@Component({
  selector: 'app-top-book-list',
  imports: [
    Loader,
    BookItem
  ],
  templateUrl: './top-book-list.html',
  styleUrl: './top-book-list.scss'
})
export class TopBookList implements OnInit {

  topBooks: BookItemModel[] = [];
  isLoading: boolean = false;

  constructor(
    private publicBooksService: PublicBooksService,
    private adminBooksService: AdminBooksService,
  ) {
  }

  ngOnInit() {
    this.loadBooks();
  }

  loadBooks() {
    this.isLoading = true;
    this.publicBooksService.getTopBooks().subscribe({
      next: (response) => {
        this.topBooks = response.books;
      },
      error: (err) => {
        console.log(err)
        this.isLoading = false;
      },
      complete: () => {
        this.isLoading = false;
      }
    })
  }

  onBookDeleted(bookId: number) {
    deleteBookWithReload(
      bookId,
      id => this.adminBooksService.deleteBook(id),
      () => this.loadBooks(),
      (loading) => this.isLoading = loading
    )
  }

}
