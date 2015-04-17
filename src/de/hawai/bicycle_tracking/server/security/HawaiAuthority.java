package de.hawai.bicycle_tracking.server.security;

import org.springframework.security.core.GrantedAuthority;

public class HawaiAuthority implements GrantedAuthority {
    @Override
    public String getAuthority() {
        return "USER";
    }
}
