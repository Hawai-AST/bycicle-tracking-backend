package de.hawai.bicycle_tracking.server.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

public interface IApplication {
	String getClientID();

	String getSecret();

	Set<String> getScopes();

	Collection<GrantedAuthority> getAuthorities();
}
