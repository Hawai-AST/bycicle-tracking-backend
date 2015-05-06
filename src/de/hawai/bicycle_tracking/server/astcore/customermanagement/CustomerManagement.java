package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class CustomerManagement implements ICustomerManagement {

	@Autowired
	private IUserDao userDao;

	@Override
	public Optional<IUser> getUserBy(EMail eMail) {
		return Optional.ofNullable(userDao.getByMailAddress(eMail).orElse(null));
	}

	@Override
	public IUser registerUser(String name, String firstName, EMail eMailAddress, Address address, Date birthdate, String password,
			  GrantedAuthority authority) {
		return userDao.save(new User(name, firstName, eMailAddress, address, birthdate, BCrypt.hashpw(password, BCrypt.gensalt()), authority));
	}

}
