package de.hawai.bicycle_tracking.server.security;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class HawaiAuthority implements GrantedAuthority {
    public static final HawaiAuthority USER = new HawaiAuthority("USER");
    public static final HawaiAuthority ADMIN = new HawaiAuthority("ADMIN");

    @Id
    @Column(name = "authority_name")
    private String authority;

    public HawaiAuthority() {

    }

    public HawaiAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

    protected void setAuthority(String authority) {
        this.authority = authority;
    }
}
