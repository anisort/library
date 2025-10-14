import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CommonModule, NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-chat-input',
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './chat-input.html',
  styleUrl: './chat-input.scss'
})
export class ChatInput implements OnInit{
  @Input() control!: FormControl;
  @Input() initialFile?: File;
  @Output() submitPrompt = new EventEmitter<{ text: string, file?: File }>();

  file?: File;
  preview?: string;

  private readonly allowedTypes = [
    'image/png',
    'image/jpeg',
    'image/jpg',
    'application/pdf'
  ];


  ngOnInit() {
    if (this.initialFile) {
      this.setFile(this.initialFile);
    }
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length) {
      const file = input.files[0];
      if (!this.allowedTypes.includes(file.type)) {
        alert('Unsupported file type. Please upload an PNG, JPEGm JPG or PDF file.');
        input.value = '';
        return;
      }
      this.setFile(file);
    }
  }

  private setFile(file: File) {
    this.file = file;
    if (file.type.startsWith('image/')) {
      const reader = new FileReader();
      reader.onload = () => {
        this.preview = reader.result as string;
      };
      reader.readAsDataURL(file);
    } else {
      this.preview = undefined;
    }
  }

  isSendDisabled(): boolean {
    const textEmpty = !this.control.value?.trim();
    return textEmpty && !this.file;
  }

  removeFile() {
    this.file = undefined;
    this.preview = undefined;
  }


  submit() {
    if (this.control.valid || this.file) {
      this.submitPrompt.emit({ text: this.control.value, file: this.file });
      this.control.reset();
      this.file = undefined;
      this.preview = undefined;
    }
  }
}
