import {Component, OnDestroy, OnInit} from '@angular/core';
import {BookSingleItemModel} from '../../models/book-single-item.model';
import {map, Subject, switchMap, takeUntil} from 'rxjs';
import {PublicBooksService} from '../../services/public/public-books.service';
import {AuthService} from '../../../../core/services/auth/auth.service';
import {ActivatedRoute} from '@angular/router';
import {UserBooksService} from '../../services/user/user-books.service';
import {Loader} from '../../../../shared/components/loader/loader';
import {NgClass, NgOptimizedImage} from '@angular/common';
import {MatDialog} from '@angular/material/dialog';
import {BookStatusModal} from '../book-status-modal/book-status-modal';
import { BookStatusUtils } from '../../utils/book-status.utils';

@Component({
  selector: 'app-book-details',
  imports: [
    Loader,
    NgOptimizedImage,
    NgClass
  ],
  templateUrl: './book-details.html',
  styleUrl: './book-details.scss',
  standalone: true
})
export class BookDetails implements OnInit, OnDestroy {

  book!: BookSingleItemModel;
  errorMessage: string = "";
  isAuthenticated: boolean = false;
  bookStatus: string | null = null;
  isLoading: boolean = false;
  isAdded: boolean = false;
  private destroy$ = new Subject<void>();

  constructor(
    private publicBookService: PublicBooksService,
    private userBookService: UserBooksService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) {
  }

  ngOnInit() {
    this.loadBook();
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadBook(){
    this.isAuthenticated = this.authService.isAuthenticated();
    this.isLoading = true;
    this.route.paramMap.pipe(
      takeUntil(this.destroy$),
      map(params => Number(params.get('id'))),
      switchMap(id => {
        return this.publicBookService.getBookById(id);
      })
    ).subscribe(response => {
      if(response) {
        this.book = response;
        if (this.isAuthenticated){
          this.userBookService.getBookStatusInUserLibrary(this.book.id).subscribe({
            next: (response) => {
              this.bookStatus = response;
              if (this.bookStatus) {
                this.isAdded = true;
              }
            },
            error: (err) => {
              console.log(err)
              this.errorMessage = 'Failed to check add status';
            }
          })
        }
      }
      else {
        this.errorMessage = 'Book not found';
      }
      this.isLoading = false;
    })
  }

  openStatusModal() {
    const dialogRef = this.dialog.open(BookStatusModal, {
      data: {
        currentStatus: this.bookStatus,
        isAdded: this.isAdded
      }
    });

    dialogRef.componentInstance.confirm.subscribe((status: string) => {
      this.isLoading = true;
      if (this.bookStatus) {
        this.userBookService.updateBookStatus(this.book.id, { bookStatus: status }).subscribe({
          next: () => this.bookStatus = status,
          error: () => this.errorMessage = 'Failed to update book status'
        });
      } else {
        this.userBookService.addBookToUserLibrary(this.book.id, { bookStatus: status }).subscribe({
          next: () => {
            this.bookStatus = status;
            this.isAdded = true;
          },
          error: () => this.errorMessage = 'Failed to add book'
        });
      }
      dialogRef.close();
      this.isLoading = false;
    });

    dialogRef.componentInstance.remove.subscribe(() => {
      this.isLoading = true;
      this.onRemoveFromUserLib();
      dialogRef.close();
      this.isLoading = false;
    });
  }

  onRemoveFromUserLib() {
    if (this.book) {
      this.isLoading = true;
      this.userBookService.removeBookFromUserLibrary(this.book.id).subscribe({
        next: () => {
          this.bookStatus = null;
          this.isAdded = false;
        },
        error: (err) => {
          console.error(err);
          this.errorMessage = 'Failed to remove';
        },
        complete: () => this.isLoading = false
      });
    }
  }

  getStatusClasses() {
    return BookStatusUtils.getStatusClasses(this.bookStatus ?? '');
  }

  formatStatus(): string {
    return BookStatusUtils.formatBookStatus(this.bookStatus ?? '');
  }
}
