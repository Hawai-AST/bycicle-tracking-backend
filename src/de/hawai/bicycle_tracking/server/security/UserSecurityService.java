package de.hawai.bicycle_tracking.server.security;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.User;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.UserDao;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityService implements UserDetailsService {

    @Autowired
    private UserDao userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            EMail mail = new EMail(username);
            User user = this.userRepository.getByeMailAddress(mail);
            return new SecurityUser(user);
        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("Couldn't find " + username);
        }
    }
}
