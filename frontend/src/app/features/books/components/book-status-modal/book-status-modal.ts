import {Component, EventEmitter, Inject, Input, OnInit, Output} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {BookStatusUtils} from '../../utils/book-status.utils';

@Component({
  selector: 'app-book-status-modal',
  imports: [
    FormsModule
  ],
  templateUrl: './book-status-modal.html',
  styleUrl: './book-status-modal.scss'
})
export class BookStatusModal implements OnInit {

  @Input() currentStatus: string | null = null;
  @Input() isAdded: boolean = false;
  @Output() confirm = new EventEmitter<string>();
  @Output() remove = new EventEmitter<void>();

  statuses = ['TO_READ', 'READING', 'READ', 'FAVORITE'];
  selectedStatus: string = '';
  showConfirmButton: boolean = false;

  constructor(@Inject(MAT_DIALOG_DATA) public data: { currentStatus: string | null; isAdded: boolean }) {
    this.currentStatus = data.currentStatus;
    this.isAdded= data.isAdded;
  }

  get isBookInLibrary(): boolean {
    return this.currentStatus !== null;
  }

  ngOnInit() {
    this.selectedStatus = this.currentStatus ?? this.statuses[0];
    this.showConfirmButton = !this.isAdded;
  }

  onStatusChange() {
    this.showConfirmButton = !this.isAdded || this.selectedStatus !== this.currentStatus;

  }

  onConfirm() {
    this.confirm.emit(this.selectedStatus);
  }

  onRemove() {
    if (confirm('Are you sure you want to remove the book from your library?')) {
      this.remove.emit();
    }
  }

  formatStatus(status: string): string {
    return BookStatusUtils.formatBookStatus(status);
  }

}
