import {Injectable} from '@angular/core';
import {AbstractControl, AsyncValidatorFn, ValidationErrors, ValidatorFn} from '@angular/forms';
import {UserValidationService} from '../services/uservalidation/user-validation.service';
import {catchError, map, of, switchMap, timer} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CustomValidator {

  static passwordStrengthValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value: string = control.value || '';
      const hasUpperCase = /[A-Z]/.test(value);
      const hasLowerCase = /[a-z]/.test(value);
      const hasNumber = /\d/.test(value);
      const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(value);

      const isValid = hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar;
      return !isValid ? { weakPassword: true } : null;
    };
  }

  static passwordMatchValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const password = control.get('password')?.value;
      const repeatPassword = control.get('repeatPassword')?.value;

      return password === repeatPassword ? null : { passwordMismatch: true };
    };
  }

  static usernameOrEmailUnique(usersService: UserValidationService): AsyncValidatorFn {
    return (control: AbstractControl) => {
      const raw = (control.value || '').trim();
      if (!raw) {
        return of(null);
      }
      return timer(400).pipe(
        switchMap(() => {
          const field = raw.includes('@') ? 'email' : 'username';
          return usersService.checkUsernameOrEmail(raw, field).pipe(
            map(res => (res.exists ? { usernameOrEmailTaken: true } : null)),
            catchError(() => of(null))
          );
        })
      );
    };
  }



}
