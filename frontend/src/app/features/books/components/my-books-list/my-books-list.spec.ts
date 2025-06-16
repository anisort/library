import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyBooksList } from './my-books-list';

describe('MyBooksList', () => {
  let component: MyBooksList;
  let fixture: ComponentFixture<MyBooksList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyBooksList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyBooksList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
