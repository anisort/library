import {Component, OnInit} from '@angular/core';
import {UserBooksService} from '../../services/user/user-books.service';
import {BookItem} from '../book-item/book-item';
import {Loader} from '../../../../shared/components/loader/loader';
import {Paginator} from '../../../../shared/components/paginator/paginator';
import {Selector} from '../../../../shared/components/selector/selector';
import {MyBookItemModel} from '../../models/my-book-item.model';
import {BookStatusUtils} from '../../utils/book-status.utils';
import {AdminBooksService} from '../../services/admin/admin-books.service';
import { deleteBookWithReload } from '../../utils/delete-book-with-reload';

@Component({
  selector: 'app-my-books-list',
  imports: [
    BookItem,
    Loader,
    Paginator,
    Selector
  ],
  templateUrl: './my-books-list.html',
  styleUrl: './my-books-list.scss'
})
export class MyBooksList implements OnInit {

  myBooks: MyBookItemModel[] = [];
  page: number = 0;
  size: number = 10;
  sort: string = 'book.title,asc';
  bookStatus: string = '';
  totalPages: number = 0;
  isLoading: boolean = false;
  rawStatuses: string[] = ['TO_READ', 'READING', 'READ', 'FAVORITE'];

  statuses: { value: string, label: string }[] = [
    { value: '', label: 'All' },
    ...this.rawStatuses.map(status => ({
      value: status,
      label: BookStatusUtils.formatBookStatus(status)
    }))
  ];


  constructor(
    private userBooksService: UserBooksService,
    private adminBooksService: AdminBooksService
  ) {
  }

  ngOnInit() {
    this.loadBooks();
  }

  loadBooks() {
    this.isLoading = true;
    this.userBooksService.getUserBooks(this.page, this.size, this.sort, this.bookStatus).subscribe({
      next: (response) => {
        this.myBooks = response.content;
        this.totalPages = response.totalPages;
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

  onPageChange(newPage: number) {
    this.page = newPage;
    this.loadBooks();
  }

  onStatusChange(bookStatus: string) {
    this.bookStatus = bookStatus === 'All' ? '' : bookStatus;
    this.page = 0;
    this.loadBooks();
  }

  onBookDeleted(bookId: number) {
    deleteBookWithReload(
      bookId,
      id => this.adminBooksService.deleteBook(id),
      () => this.loadBooks(),
      (loading) => this.isLoading = loading
    );
  }

}
