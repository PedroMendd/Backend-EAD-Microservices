package com.ead.authuser.configs.security;

import com.ead.authuser.models.UserModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserDetailsImpl implements UserDetails {

    private UUID userId;
    private String fullName;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(UUID userId, String fullName, String username, String password,
                           String email, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(UserModel userModel) {
        List<GrantedAuthority> authorities = userModel.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(
                userModel.getUserId(),
                userModel.getFullName(),
                userModel.getUsername(),
                userModel.getPassword(),
                userModel.getEmail(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
