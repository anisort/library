package com.auth.dto;

import com.auth.advice.validators.unique.Unique;
import com.auth.advice.validators.unique.UserExistsValidationService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateUsernameDto {

    @NotBlank(message = "username is required")
    @Size(min = 3, message = "min length equals 3")
    @Unique(service = UserExistsValidationService.class, fieldName = "username", message = "username already exists")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
