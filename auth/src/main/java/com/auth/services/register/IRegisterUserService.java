package com.auth.services.register;

import com.auth.dto.RegisterUserDto;
import com.auth.dto.UserInfoDto;

public interface IRegisterUserService {

    UserInfoDto register(RegisterUserDto registerUserDto);
}
