import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookStatusModal } from './book-status-modal';

describe('BookStatusModal', () => {
  let component: BookStatusModal;
  let fixture: ComponentFixture<BookStatusModal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookStatusModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookStatusModal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
