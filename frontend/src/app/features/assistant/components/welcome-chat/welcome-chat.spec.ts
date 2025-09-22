import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WelcomeChat } from './welcome-chat';

describe('WelcomeChat', () => {
  let component: WelcomeChat;
  let fixture: ComponentFixture<WelcomeChat>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WelcomeChat]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WelcomeChat);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
