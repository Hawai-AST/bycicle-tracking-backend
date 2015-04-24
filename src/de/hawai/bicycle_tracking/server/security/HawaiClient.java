package de.hawai.bicycle_tracking.server.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.*;

public class HawaiClient implements ClientDetails {

    private static final int VALIDITY_TIME = 60 * 60 * 24 * 7;

    private final IApplication application;

    public HawaiClient(IApplication application) {
        this.application = application;
    }

    @Override
    public String getClientId() {
        return this.application.getClientID();
    }

    @Override
    public Set<String> getResourceIds() {
        return Collections.singleton("hawai-ast");
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public String getClientSecret() {
        return this.application.getSecret();
    }

    @Override
    public boolean isScoped() {
        return true;
    }

    @Override
    public Set<String> getScope() {
        return this.application.getScopes();
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return Collections.singleton("password");
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return null;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.application.getAuthorities();
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return VALIDITY_TIME;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return VALIDITY_TIME * 2;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return this.getAuthorities().contains(new HawaiAuthority("ADMIN"));
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return new HashMap<>();
    }
}
