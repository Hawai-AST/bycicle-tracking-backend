package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;

@Component
public class CustomerManager implements ICustomerManagement {

	@Autowired
	private UserDao userDao;

	@Override
	public Optional<IUser> getUserBy(EMail eMail) {
		return Optional.ofNullable(userDao.getByeMailAddress(eMail).orElse(null));
	}

	@Override
	public IUser register(String name, String firstName, String eMailAddress, Address address, Date birthdate, String password) {
		return userDao.save(new User(name, firstName, new EMail(eMailAddress), address, birthdate, password));
	}

}
