package com.auth.services.userinfo;

import com.auth.dto.UserInfoDto;

public interface UserInfoService {

    UserInfoDto getUserInfo(Long id);

    UserInfoDto updateUsername(Long id, String username);

    UserInfoDto updateAvatar(Long id, String newAvatar);
}
