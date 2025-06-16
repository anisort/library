import { TestBed } from '@angular/core/testing';

import { PublicBooksService } from './public-books.service';

describe('PublicBooksService', () => {
  let service: PublicBooksService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PublicBooksService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
