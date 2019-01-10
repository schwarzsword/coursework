package com.schwarzsword.pip.coursework.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, BANNED, EXPERT;

    @Override
    public String getAuthority() {
        return name();
    }
}
