import { TestBed } from '@angular/core/testing';

import { InitialPromptService } from './initial-prompt.service';

describe('InitialPromptService', () => {
  let service: InitialPromptService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InitialPromptService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
