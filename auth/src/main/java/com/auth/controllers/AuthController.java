package com.auth.controllers;

import com.auth.dto.RegisterUserDto;
import com.auth.dto.UserInfoDto;
import com.auth.services.register.IRegisterUserService;
import com.auth.services.register.RegisterUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IRegisterUserService registerUserService;

    @Autowired
    public AuthController(IRegisterUserService registerUserService) {
        this.registerUserService = registerUserService;
    }

    @PostMapping("/register")
    public UserInfoDto registerUser(@RequestBody @Valid RegisterUserDto registerUserDto) {
        return registerUserService.register(registerUserDto);
    }
}

