package com.auth.utils.components;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Set;

@Getter
@Component
public class AvatarConfig {
    private final Set<String> allowedAvatars = Set.of(
            "/avatars/girlhat.png",
            "/avatars/woman.png",
            "/avatars/robot.png",
            "/avatars/gamer.png",
            "/avatars/girl.png",
            "/avatars/boy.png"
    );

}

