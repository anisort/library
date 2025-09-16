package com.auth.converters;

import com.auth.dto.UserInfoDto;
import com.auth.entities.CustomUserDetails;

public class UsersConverter {

    public static UserInfoDto convertUserToUserInfoDto(CustomUserDetails user) {
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setId(user.getId());
        userInfoDto.setUsername(user.getUsername());
        userInfoDto.setEmail(user.getEmail());
        userInfoDto.setName(user.getName());
        userInfoDto.setRole(user.getRole());
        userInfoDto.setAvatar(user.getAvatar());
        return userInfoDto;
    }
}
