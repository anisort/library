import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {RegisterService} from '../../services/register/register.service';
import {AuthService} from '../../../../core/services/auth/auth.service';
import {Loader} from '../../../../shared/components/loader/loader';
import {CustomValidator} from '../../validators/custom.validator';
import {UserValidationService} from '../../services/uservalidation/user-validation.service';

@Component({
  selector: 'app-register',
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    Loader
  ],
  templateUrl: './register.html',
  styleUrl: './register.scss',
  standalone: true
})
export class Register implements OnInit {

  registerForm!: FormGroup;
  isLoading: boolean = false;
  message: string | null = null;

  constructor(
    private registerService: RegisterService,
    private authService: AuthService,
    private userValidationService: UserValidationService
  ) {}

  ngOnInit() {
    this.initForm();
  }

  initForm() {
    this.registerForm = new FormGroup({
        username: new FormControl('', [Validators.required, Validators.minLength(3)], [CustomValidator.usernameOrEmailUnique(this.userValidationService)]),
        email: new FormControl('', [Validators.required, Validators.email], [CustomValidator.usernameOrEmailUnique(this.userValidationService)]),
        password: new FormControl('', [Validators.required, Validators.minLength(8), CustomValidator.passwordStrengthValidator()]),
        repeatPassword: new FormControl('', [Validators.required])
      },
      { validators: CustomValidator.passwordMatchValidator() }
    )
  }

  onRegister() {
    if(this.registerForm.valid){
      this.isLoading = true;
      this.registerService.register(this.registerForm.value).subscribe({
        next: () => {
          this.message = "Successfully registered";
          this.registerForm.reset();
        },
        error: (err) => {
          console.log(err)
          this.message = "Error to register";
          this.isLoading = false;
        },
        complete: () => {
          this.isLoading = false;
        }
      })
    }
  }

  onLogin() {
    this.authService.login();
  }
}
