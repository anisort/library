package com.auth.services.register;

import com.auth.dto.RegisterUserDto;
import com.auth.dto.UserInfoDto;

public interface RegisterUserService {

    UserInfoDto register(RegisterUserDto registerUserDto);
}
