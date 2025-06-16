package com.auth.utils.components;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AvatarConfig {
    private final Set<String> allowedAvatars = Set.of(
            "https://storage.googleapis.com/user-standart-images-bucket/boy%20(1).png",
            "https://storage.googleapis.com/user-standart-images-bucket/boy.png",
            "https://storage.googleapis.com/user-standart-images-bucket/gamer.png",
            "https://storage.googleapis.com/user-standart-images-bucket/girl%20(1).png",
            "https://storage.googleapis.com/user-standart-images-bucket/girl%20(2).png",
            "https://storage.googleapis.com/user-standart-images-bucket/girl%20(3).png",
            "https://storage.googleapis.com/user-standart-images-bucket/girl%20(4).png",
            "https://storage.googleapis.com/user-standart-images-bucket/girl.png",
            "https://storage.googleapis.com/user-standart-images-bucket/man%20(1).png",
            "https://storage.googleapis.com/user-standart-images-bucket/man%20(2).png",
            "https://storage.googleapis.com/user-standart-images-bucket/man%20(3).png",
            "https://storage.googleapis.com/user-standart-images-bucket/man.png",
            "https://storage.googleapis.com/user-standart-images-bucket/robot.png",
            "https://storage.googleapis.com/user-standart-images-bucket/woman%20(1).png",
            "https://storage.googleapis.com/user-standart-images-bucket/woman%20(2).png",
            "https://storage.googleapis.com/user-standart-images-bucket/woman%20(3).png",
            "https://storage.googleapis.com/user-standart-images-bucket/woman.png"
    );

    public Set<String> getAllowedAvatars() {
        return allowedAvatars;
    }
}

