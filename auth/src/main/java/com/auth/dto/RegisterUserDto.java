package com.auth.dto;

import com.auth.advice.validators.unique.Unique;
import com.auth.advice.validators.unique.UserExistsValidationService;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterUserDto {

    @NotBlank(message = "username is required")
    @Size(min = 3, message = "min length equals 3")
    @Unique(service = UserExistsValidationService.class, fieldName = "username", message = "username already exists")
    private String username;

    @NotBlank(message = "email is required")
    @Email(message = "invalid email")
    @Unique(service = UserExistsValidationService.class, fieldName = "email", message = "email already exists")
    private String email;

    @NotBlank(message = "username is required")
    @Size(min = 3, message = "min length equals 3")
    private String name;

    @NotBlank(message = "password is required")
    @Pattern(regexp = "(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*])(?=.*\\d).{8,}", message = "Password must be at least 8 characters long, contain one letter, one number, and one special character !@#$%^&*")
    private String password;

    @NotBlank(message = "password repetition is required")
    private String repeatPassword;

    @AssertTrue(message = "passwords don't match")
    private boolean isPasswordsMatch() {
        return password.equals(repeatPassword);
    }
}
