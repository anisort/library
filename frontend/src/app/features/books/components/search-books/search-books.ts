import {Component, ElementRef, HostListener, ViewChild} from '@angular/core';
import {BookItemModel} from '../../models/book-item.model';
import {PublicBooksService} from '../../services/public/public-books.service';
import {Loader} from '../../../../shared/components/loader/loader';
import {Selector} from '../../../../shared/components/selector/selector';
import {FormsModule} from '@angular/forms';
import {SearchBookItem} from '../search-book-item/search-book-item';

@Component({
  selector: 'app-search-books',
  imports: [
    Loader,
    Selector,
    FormsModule,
    SearchBookItem
  ],
  templateUrl: './search-books.html',
  styleUrl: './search-books.scss'
})
export class SearchBooks {

  searchBooks: BookItemModel[] = [];
  searchText: string = '';
  filter: string = 'title';
  isSearchFocused: boolean = false;
  isLoading: boolean = false;
  rawFilters: string[] = ['Title', 'Author'];

  filters: { value: string, label: string }[] = [
    ...this.rawFilters.map(filter => ({
      value: filter.toLowerCase(),
      label: filter
    }))
  ]

  @ViewChild('searchContainer') searchContainer!: ElementRef;

  constructor(
    private publicBooksService: PublicBooksService
  ) {
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent) {
    if (!this.searchContainer.nativeElement.contains(event.target)) {
      this.isSearchFocused = false;
    }
  }

  onSearchFocus() {
    this.isSearchFocused = true;
  }

  onSearchChange() {
    if (this.searchText.trim() === '') {
      this.searchBooks = [];
    } else {
      this.loadBooks();
    }
  }

  onFilterChange(filter: string) {
    this.filter = filter;
    if (this.searchText.trim()) {
      this.loadBooks();
    }
  }

  loadBooks() {
    this.isLoading = true;
    this.publicBooksService.searchBooks(this.filter, this.searchText).subscribe({
      next: (response) => {
        this.searchBooks = response;
      },
      error: (err) => {
        console.log(err)
        this.isLoading = false;
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }
}
