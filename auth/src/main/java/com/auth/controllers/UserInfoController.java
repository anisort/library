package com.auth.controllers;

import com.auth.dto.UpdateAvatarDto;
import com.auth.dto.UpdateUsernameDto;
import com.auth.dto.UserInfoDto;
import com.auth.services.userinfo.UserInfoService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/userinfo")
public class UserInfoController {

    private final UserInfoService userServiceInfo;

    public UserInfoController(UserInfoService userServiceInfo) {
        this.userServiceInfo = userServiceInfo;
    }

    @GetMapping
    public UserInfoDto getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        return userServiceInfo.getUserInfo(userId);
    }

    @PatchMapping("/username")
    public UserInfoDto updateUsername(@RequestBody @Valid UpdateUsernameDto updateUsernameDto, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        return userServiceInfo.updateUsername(userId, updateUsernameDto.getUsername());
    }

    @PatchMapping("/avatar")
    public UserInfoDto updateAvatar(@RequestBody @Valid UpdateAvatarDto updateAvatarDto, @AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.valueOf(jwt.getSubject());
        return userServiceInfo.updateAvatar(userId, updateAvatarDto.getAvatar());
    }
}
