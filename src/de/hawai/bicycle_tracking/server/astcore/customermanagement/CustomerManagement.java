package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import java.util.Date;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import de.hawai.bicycle_tracking.server.utility.value.Gender;

@Component
public class CustomerManagement implements ICustomerManagement {

	@Autowired
	@Qualifier("userDAO")
	private IUserDao userDao;

	@Override
	public Optional<IUser> getUserBy(EMail eMail) {
		return Optional.ofNullable(userDao.getByMailAddress(eMail).orElse(null));
	}

	@Override
	public IUser registerUser(String name, String firstName, EMail eMailAddress, Address address, Date birthdate, String password,
							  Gender gender, GrantedAuthority authority) {
		return userDao.save(new User(name, firstName, eMailAddress, address, birthdate, BCrypt.hashpw(password, BCrypt.gensalt()),
				gender, authority));
	}

	@Override
	public void updatePassword(IUser user, String password) {
		User actualUser = (User) user;
		actualUser.setPassword(password);
		userDao.save(actualUser);
	}

	@Override
	public void updateUser(IUser user, String name, String firstName, Date birthday, Address address) {
		User actualUser = (User) user;
		actualUser.setName(name);
		actualUser.setFirstName(firstName);
		actualUser.setBirthdate(birthday);
		actualUser.setAddress(address);
		userDao.save(actualUser);
	}
}
