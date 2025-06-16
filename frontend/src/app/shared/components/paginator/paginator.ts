import {Component, EventEmitter, Input, OnChanges, Output} from '@angular/core';

@Component({
  selector: 'app-paginator',
  imports: [],
  templateUrl: './paginator.html',
  styleUrl: './paginator.scss'
})
export class Paginator implements OnChanges {
  @Input() currentPage: number = 0;
  @Input() totalPages: number = 1;

  @Output() pageChange = new EventEmitter<number>();

  inputValue: string = '';

  ngOnChanges() {
    this.inputValue = (this.currentPage + 1).toString();
  }

  goToPage(page: number) {
    if (page < 0) page = 0;
    if (page >= this.totalPages) page = this.totalPages - 1;
    this.pageChange.emit(page);
  }

  prev() {
    this.goToPage(this.currentPage - 1);
  }

  next() {
    this.goToPage(this.currentPage + 1);
  }

  onInputChange(event: Event) {
    const value = +(event.target as HTMLInputElement).value;
    if (!isNaN(value)) {
      const page = Math.min(Math.max(1, value), this.totalPages);
      this.goToPage(page - 1);
    }
  }
}
