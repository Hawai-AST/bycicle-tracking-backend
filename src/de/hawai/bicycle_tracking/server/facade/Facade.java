package de.hawai.bicycle_tracking.server.facade;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.ICustomerManagement;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;

@Component
public class Facade {

	@Autowired
	private ICustomerManagement customerManegement;

	private Facade() {
		super();
	}

	public Optional<IUser> getUserBy(EMail eMail) {
		return customerManegement.getUserBy(eMail);
	}

	public IUser registerUser(String name, String firstName, EMail eMailAddress, Address address, Date birthdate, String password) {
		return customerManegement.registerUser(name, firstName, eMailAddress, address, birthdate, password);
	}

}
