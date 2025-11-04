package com.auth.services.register;

import com.auth.utils.converters.UsersConverter;
import com.auth.dto.RegisterUserDto;
import com.auth.dto.UserInfoDto;
import com.auth.entities.CustomUserDetails;
import com.auth.repositories.UserRepository;
import com.auth.utils.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserServiceImpl implements RegisterUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterUserServiceImpl(
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
                registerUserDto.getName(),
                passwordEncoder.encode(registerUserDto.getPassword()),
                Role.USER,
                "/avatars/user.png"
        );
        CustomUserDetails savedUser = userRepository.save(customUserDetails);
        return UsersConverter.convertUserToUserInfoDto(savedUser);
    }
}