package com.bookstorehassouf.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public class Authority implements GrantedAuthority {

    private final String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}
