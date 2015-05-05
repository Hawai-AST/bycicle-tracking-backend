package de.hawai.bicycle_tracking.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

@Service
public class ClientSecurityService implements ClientDetailsService {

    @Autowired
    private IApplicationDao applicationDao;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        IApplication application = applicationDao.getByClientID(clientId);
        return new HawaiClient(application);
    }
}
