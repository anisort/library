import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AdminBooksService} from '../../services/admin/admin-books.service';
import {CreateBookModel} from '../../models/create-book.model';
import {ActivatedRoute, Router} from '@angular/router';
import {Loader} from '../../../../shared/components/loader/loader';
import {PublicBooksService} from '../../services/public/public-books.service';
import {finalize, map, Subject, switchMap, takeUntil} from 'rxjs';

@Component({
  selector: 'app-book-form',
  imports: [
    ReactiveFormsModule,
    Loader
  ],
  templateUrl: './book-form.html',
  styleUrl: './book-form.scss',
  standalone: true
})
export class BookForm implements OnInit, OnDestroy {

  bookForm!: FormGroup;
  isLoading: boolean = false;
  coverPreview: string | null = null;
  isEditMode = false;
  bookId?: number;
  private destroy$ = new Subject<void>();

  constructor(
    private adminBooksService: AdminBooksService,
    private publicBookService: PublicBooksService,
    private router: Router,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.bookId = Number(id);
      this.isEditMode = true;
    }
    this.initForm();
    if (this.isEditMode) {
      this.loadBook();
    }
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  initForm() {
    this.bookForm = new FormGroup({
      title: new FormControl('', [Validators.required, Validators.minLength(3)]),
      author: new FormControl('', [Validators.required, Validators.minLength(3)]),
      summary: new FormControl('', [Validators.required, Validators.minLength(3)]),
      link: new FormControl('', [Validators.required]),
      file: new FormControl(null)
    });

    if (!this.isEditMode) {
      this.bookForm.get('file')?.addValidators(Validators.required);
    }
  }


  loadBook() {
    this.route.paramMap.pipe(
      takeUntil(this.destroy$),
      map(params => Number(params.get('id'))),
      switchMap(id => this.publicBookService.getBookById(id)),
      finalize(() => this.isLoading = false)
    ).subscribe({
      next: (response) => {
        this.bookForm.patchValue({
          title: response.title,
          author: response.author,
          summary: response.summary,
          link: response.link
        });
        if (response.coverLink) {
          this.coverPreview = response.coverLink;
        }
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  onFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      const file = input.files[0];
      this.bookForm.patchValue({ file });

      const reader = new FileReader();
      reader.onload = () => {
        this.coverPreview = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  onSubmit() {
    if (this.bookForm.valid) {
      this.isLoading = true;
      const { title, author, summary, link, file } = this.bookForm.value;
      const book: CreateBookModel = { title, author, summary, link };
      const formData = new FormData();
      formData.append('book', new Blob([JSON.stringify(book)], { type: 'application/json' }));
      if (file) {
        formData.append('file', file);
      }

      const request$ = this.isEditMode && this.bookId
        ? this.adminBooksService.updateBook(this.bookId, formData)
        : this.adminBooksService.createBook(formData);

      request$.pipe(finalize(() => this.isLoading = false)).subscribe({
        next: () => {
          this.router.navigate(['/catalog']).catch(err => console.error('Navigation error:', err));
        },
        error: (err) => {
          console.error(err);
        }
      });
    }
  }
}
