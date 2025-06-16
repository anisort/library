package com.auth.services.userinfo;

import com.auth.converters.UsersConverter;
import com.auth.dto.UserInfoDto;
import com.auth.entities.CustomUserDetails;
import com.auth.repositories.UserRepository;
import com.auth.utils.components.AvatarConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserInfoService implements IUserServiceInfo {

    private final UserRepository userRepository;
    private final AvatarConfig avatarConfig;

    @Autowired
    public UserInfoService(UserRepository userRepository, AvatarConfig avatarConfig) {
        this.userRepository = userRepository;
        this.avatarConfig = avatarConfig;
    }

    @Override
    public UserInfoDto getUserInfo(Long id) {
        CustomUserDetails userDetails = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + id));
        return UsersConverter.convertUserToUserInfoDto(userDetails);
    }

    @Override
    public UserInfoDto updateUsername(Long id, String username) {
        CustomUserDetails userDetails = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + id));
        userDetails.setUsername(username);
        userRepository.save(userDetails);
        return UsersConverter.convertUserToUserInfoDto(userDetails);
    }

    @Override
    public UserInfoDto updateAvatar(Long id, String newAvatar) {
        if (!avatarConfig.getAllowedAvatars().contains(newAvatar)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid avatar selected");
        }
        CustomUserDetails userDetails = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + id));
        userDetails.setAvatar(newAvatar);
        userRepository.save(userDetails);
        return UsersConverter.convertUserToUserInfoDto(userDetails);
    }
}
