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
  @Input() isEditing = false;
  @Output() startEdit = new EventEmitter<void>();
  @Output() cancelEdit = new EventEmitter<void>();

  @ViewChild('menu') menu!: ElementRef;
  @ViewChild('editContainer') editContainer!: ElementRef;

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

  requestDelete() {
    this.remove.emit(this.chat);
  }

  @HostListener('document:click', ['$event'])
  onClickOutside(event: MouseEvent) {

    const target = event.target as Node;
    const clickedInsideMenu = this.menu?.nativeElement.contains(target);
    const clickedInsideEdit = this.editContainer?.nativeElement.contains(target);

    if (this.isOpen && !clickedInsideMenu) {
      this.menuToggle.emit();
    }

    if (this.isEditing && !clickedInsideEdit) {
      this.cancelEdit.emit();
    }
  }

  toggleMenu(event: MouseEvent) {
    event.stopPropagation();
    this.menuToggle.emit();
  }
}
