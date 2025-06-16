package com.auth.services.register;

import com.auth.converters.UsersConverter;
import com.auth.dto.RegisterUserDto;
import com.auth.dto.UserInfoDto;
import com.auth.entities.CustomUserDetails;
import com.auth.repositories.UserRepository;
import com.auth.utils.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserService implements IRegisterUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterUserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserInfoDto register(RegisterUserDto registerUserDto) {
        CustomUserDetails customUserDetails = new CustomUserDetails(
                registerUserDto.getUsername(),
                registerUserDto.getEmail(),
                passwordEncoder.encode(registerUserDto.getPassword()),
                Role.USER,
                "https://storage.googleapis.com/user-standart-images-bucket/user.png"
        );
        CustomUserDetails savedUser = userRepository.save(customUserDetails);
        return UsersConverter.convertUserToUserInfoDto(savedUser);
    }
}