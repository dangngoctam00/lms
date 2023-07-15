package com.example.lmsbackend.config.security;

import com.example.lmsbackend.enums.AccountTypeEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final AccountTypeEnum accountType;

    public CustomUserDetails(Long id, String username, AccountTypeEnum accountType) {
        this.id = id;
        this.username = username;
        this.accountType = accountType;
    }

    public Long getId() {
        return id;
    }

    public AccountTypeEnum getAccountType() {
        return accountType;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
