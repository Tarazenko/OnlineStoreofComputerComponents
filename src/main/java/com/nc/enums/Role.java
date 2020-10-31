package com.nc.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    MANAGER,
    ADMIN;

    Role() {
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
