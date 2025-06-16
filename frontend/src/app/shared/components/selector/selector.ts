import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-selector',
  imports: [],
  templateUrl: './selector.html',
  styleUrl: './selector.scss'
})
export class Selector {
  @Input() options: { value: string, label: string }[] = [];
  @Input() selected: string = '';
  @Output() selectedChange = new EventEmitter<string>();

  onSelect(option: string) {
    this.selectedChange.emit(option);
  }
}
