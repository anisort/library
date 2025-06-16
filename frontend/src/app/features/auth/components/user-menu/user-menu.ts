import {Component, ElementRef, HostListener, Input, ViewChild} from '@angular/core';
import {UserInfoModel} from '../../models/user-info-model';
import {AuthService} from '../../../../core/services/auth/auth.service';
import { RouterLink } from '@angular/router';
import {CommonModule, NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-user-menu',
  imports: [
    CommonModule,
    RouterLink,
    NgOptimizedImage,
  ],
  templateUrl: './user-menu.html',
  styleUrl: './user-menu.scss'
})
export class UserMenu {

  @Input() user!: UserInfoModel | null;
  readonly fallback: string = 'https://storage.googleapis.com/user-standart-images-bucket/user.png';
  isOpen: boolean = false;
  @ViewChild('menuContainer') menuContainer!: ElementRef;

  constructor(
    private authService: AuthService
  ) {}

  toggle() {
    this.isOpen = !this.isOpen;
  }

  logout() {
    this.authService.logout();
    this.isOpen = false;
  }

  @HostListener('document:click', ['$event'])
  handleClickOutside(event: MouseEvent) {
    if (!this.menuContainer.nativeElement.contains(event.target)) {
      this.isOpen = false;
    }
  }

}
