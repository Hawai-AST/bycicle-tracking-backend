package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import de.hawai.bicycle_tracking.server.utility.value.Gender;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.Optional;

public interface ICustomerManagement {

	public Optional<IUser> getUserBy(EMail eMail);

	public IUser registerUser(String name, String firstName, EMail eMailAddress, Address address, Date birthdate, String password,
							  Gender gender, GrantedAuthority authority);

	void updatePassword(IUser user, String password);

	void updateUser(IUser user, String name, String firstName, Date birthday, Address address);
}
