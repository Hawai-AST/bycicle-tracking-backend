package de.hawai.bicycle_tracking.server.security;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.User;
import de.hawai.bicycle_tracking.server.facade.Facade;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSecurityService implements UserDetailsService {

    @Autowired
    private Facade facade;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            EMail mail = new EMail(username);
            Optional<IUser> user = this.facade.getUserBy(mail);
            return new SecurityUser((User) user.get());
        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("Couldn't find " + username);
        }
    }
}
