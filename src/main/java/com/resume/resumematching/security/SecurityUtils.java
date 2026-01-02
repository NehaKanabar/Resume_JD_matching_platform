package com.resume.resumematching.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class SecurityUtils {

    public static boolean isSuperUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null &&
                auth.getAuthorities().contains(
                        new SimpleGrantedAuthority("ROLE_SUPERUSER")
                );
    }
}
