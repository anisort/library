import {Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Output, ViewChild} from '@angular/core';
import {RouterModule} from '@angular/router';
import {CommonModule} from '@angular/common';
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';

@Component({
  selector: 'app-chat-item',
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './chat-item.html',
  styleUrl: './chat-item.scss'
})
export class ChatItem implements OnInit {
  @Input() chat!: Chat;
  @Output() rename = new EventEmitter<Chat>();
  @Output() remove = new EventEmitter<Chat>();
  @Input() isOpen = false;
  @Output() menuToggle = new EventEmitter<void>();
  @Input() isEditing = false;           // управляется родителем
  @Output() startEdit = new EventEmitter<void>();
  @Output() cancelEdit = new EventEmitter<void>();

  @ViewChild('menu') menu!: ElementRef;

  titleControl!: FormControl;

  ngOnInit() {
    this.titleControl = new FormControl(this.chat.title, [Validators.required]);
  }

  startEditLocal() {
    this.titleControl.setValue(this.chat.title);
    this.startEdit.emit();
  }

  confirmEdit() {
    if (this.titleControl.invalid) return;
    this.rename.emit({ ...this.chat, title: this.titleControl.value });
  }

  cancelEditLocal() {
    this.cancelEdit.emit();
  }

  requestDelete() {
    this.remove.emit(this.chat);
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent) {
    if (this.menu && this.menu.nativeElement && !this.menu.nativeElement.contains(event.target)) {
      if (this.isOpen) this.menuToggle.emit();
    }
  }

  toggleMenu(event: MouseEvent) {
    event.stopPropagation();
    this.menuToggle.emit();
  }
}
