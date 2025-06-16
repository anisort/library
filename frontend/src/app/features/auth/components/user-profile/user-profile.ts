import {Component, ElementRef, HostListener, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {UserInfoModel} from '../../models/user-info-model';
import {Subject, takeUntil} from 'rxjs';
import {UserStoreService} from '../../../../core/services/userstore/user-store.service';
import {NgOptimizedImage} from '@angular/common';
import {userAvatars} from '../../mockdata/user-avatars';
import {MatIcon} from '@angular/material/icon';
import {FormControl, ReactiveFormsModule, Validators} from '@angular/forms';
import {UserinfoService} from '../../services/userinfo/userinfo.service';
import {Loader} from '../../../../shared/components/loader/loader';
import {CustomValidator} from '../../validators/custom.validator';
import {UserValidationService} from '../../services/uservalidation/user-validation.service';

@Component({
  selector: 'app-user-profile',
  imports: [
    NgOptimizedImage,
    MatIcon,
    ReactiveFormsModule,
    Loader
  ],
  templateUrl: './user-profile.html',
  styleUrl: './user-profile.scss'
})
export class UserProfile implements OnInit, OnDestroy {

  user: UserInfoModel | null = null;
  usernameControl!: FormControl;
  userAvatars: string[] = userAvatars;
  initialUsername: string | null = null;
  selectedAvatar: string | null = null;
  isAvatarsOpen: boolean = false;
  isUsernameEdit: boolean = false;
  isLoading: boolean = false;
  readonly fallback: string = 'https://storage.googleapis.com/user-standart-images-bucket/user.png';
  private destroy$ = new Subject<void>();
  @ViewChild('avatarsContainer') avatarsContainer!: ElementRef;

  constructor(
    private userStore: UserStoreService,
    private userinfoService: UserinfoService,
    private userValidationService: UserValidationService
  ) {}

  ngOnInit() {
    this.usernameControl = new FormControl('', [Validators.required, Validators.minLength(3)], [CustomValidator.usernameOrEmailUnique(this.userValidationService)]);
    this.userStore.user$
      .pipe(takeUntil(this.destroy$))
      .subscribe(user => this.user = user);
  }


  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  toggleUsername() {
    this.isUsernameEdit = !this.isUsernameEdit;
    if (this.user?.username) {
      this.initialUsername = this.user.username;
      this.usernameControl.setValue(this.user.username);
    }
  }

  toggleAvatars(){
    this.isAvatarsOpen = !this.isAvatarsOpen;
  }

  get isUsernameChanged(): boolean {
    return this.usernameControl.value !== this.initialUsername;
  }

  @HostListener('document:click', ['$event'])
  handleClickOutside(event: MouseEvent) {
    if (!this.avatarsContainer.nativeElement.contains(event.target)) {
      this.isAvatarsOpen = false;
    }
  }

  selectAvatar(avatar: string) {
    this.selectedAvatar = avatar;
  }

  onUpdateUsername() {
    if(this.usernameControl.valid){
      this.isLoading = true;
      this.userinfoService.updateUsername({username: this.usernameControl.value}).subscribe({
        next: (response) => {
          this.user = response;
          this.userStore.update(this.user);
        },
        error: (err) => {
          console.log(err);
          this.isLoading = false;
        },
        complete: () => {
          this.isLoading = false;
          this.toggleUsername();
        }
      })
    }
  }

  onUpdateAvatar() {
    if (this.selectedAvatar) {
      this.isLoading = true;
      this.userinfoService.updateAvatar({avatar: this.selectedAvatar}).subscribe({
        next: (response) => {
          this.user = response;
          this.userStore.update(this.user);
        },
        error: (err) => {
          console.log(err);
          this.isLoading = false;
        },
        complete: () => {
          this.isLoading = false;
        }
      })
    }
  }

}

