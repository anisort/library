import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopBookList } from './top-book-list';

describe('TopBookList', () => {
  let component: TopBookList;
  let fixture: ComponentFixture<TopBookList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TopBookList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TopBookList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
