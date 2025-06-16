import {Component, OnInit} from '@angular/core';
import {BookItemModel} from '../../models/book-item.model';
import {PublicBooksService} from '../../services/public/public-books.service';
import {Loader} from '../../../../shared/components/loader/loader';
import {BookItem} from '../book-item/book-item';
import {Paginator} from '../../../../shared/components/paginator/paginator';
import {Selector} from '../../../../shared/components/selector/selector';
import {AuthService} from '../../../../core/services/auth/auth.service';
import {Router} from '@angular/router';
import {AdminBooksService} from '../../services/admin/admin-books.service';
import { deleteBookWithReload } from '../../utils/delete-book-with-reload';

@Component({
  selector: 'app-book-list',
  imports: [
    Loader,
    BookItem,
    Paginator,
    Selector
  ],
  templateUrl: './book-list.html',
  styleUrl: './book-list.scss',
  standalone: true
})
export class BookList implements OnInit {

  books: BookItemModel[] = [];
  page: number = 0;
  size: number = 20;
  sort: string = 'title,asc';
  letter: string = '';
  totalPages: number = 0;
  // errormessage
  isLoading: boolean = false;
  rawAlphabet: string[] = ['*', ...Array.from({ length: 26 }, (_, i) => String.fromCharCode(65 + i))];

  alphabet: { value: string, label: string }[] = this.rawAlphabet.map(letter => ({
    value: letter === '*' ? '' : letter,
    label: letter
  }));

  constructor(
    private publicBooksService: PublicBooksService,
    private adminBooksService: AdminBooksService,
    private router: Router,
    public authService: AuthService
  ) {
  }

  ngOnInit() {
    this.loadBooks();
  }

  loadBooks() {
    this.isLoading = true;
    this.publicBooksService.getAllBooks(this.page, this.size, this.sort, this.letter).subscribe({
      next: (response) => {
        this.books = response.content;
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

  onLetterChange(letter: string) {
    this.letter = letter === '*' ? '' : letter;
    this.page = 0;
    this.loadBooks();
  }

  async onAdd() {
    await this.router.navigate(['/book-form']);
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
