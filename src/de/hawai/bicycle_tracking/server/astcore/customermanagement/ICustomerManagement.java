package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import java.util.Date;
import java.util.Optional;

import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;

public interface ICustomerManagement {

	public Optional<IUser> getUserBy(EMail eMail);

	public IUser registerUser(String name, String firstName, EMail eMailAddress, Address address, Date birthdate, String password);

}
