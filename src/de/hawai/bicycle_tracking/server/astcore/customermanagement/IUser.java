package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import java.util.Date;
import java.util.UUID;

import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import de.hawai.bicycle_tracking.server.utility.value.Gender;

public interface IUser {
	
	public UUID getId();

	public String getName();

	public String getFirstName();

	public Date getBirthdate();

	public Address getAddress();

	public EMail getMailAddress();

	public String getPassword();

	public Gender getGender();
}
