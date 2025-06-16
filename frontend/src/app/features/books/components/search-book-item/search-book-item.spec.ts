import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchBookItem } from './search-book-item';

describe('SearchBookItem', () => {
  let component: SearchBookItem;
  let fixture: ComponentFixture<SearchBookItem>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SearchBookItem]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchBookItem);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
