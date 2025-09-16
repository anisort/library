package com.auth.dto;

import com.auth.utils.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserInfoDto {

    private Long id;

    private String username;

    private String email;

    private String name;

    private Role role;

    private String avatar;

}
