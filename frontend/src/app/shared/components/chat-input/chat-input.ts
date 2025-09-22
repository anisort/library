import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FormControl, FormsModule, ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-chat-input',
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './chat-input.html',
  styleUrl: './chat-input.scss'
})
export class ChatInput {
  @Input() control!: FormControl;
  @Output() submitPrompt = new EventEmitter<string>();

  submit() {
    this.submitPrompt.emit();
  }

}
