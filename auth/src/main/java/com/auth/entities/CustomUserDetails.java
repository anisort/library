package com.auth.entities;

import com.auth.utils.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Setter
@Entity
@Table(name = "users")
public class CustomUserDetails implements UserDetails, CredentialsContainer {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Getter
    private String email;

    private String password;

    @Getter
    private Role role;

    @Getter
    private String avatar;

    @Getter
    private String googleSub;

    @Getter
    private String name;

    public CustomUserDetails() {}

    public CustomUserDetails(
            String username,
            String email,
            String name,
            String password,
            Role role,
            String avatar
    ) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.avatar = avatar;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getRole().name()));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
