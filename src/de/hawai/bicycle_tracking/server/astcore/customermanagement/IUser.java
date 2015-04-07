package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import java.util.Date;

import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;

public interface IUser {

	public String getName();

	public String getFirstName();

	public Date getBirthdate();

	public Address getAddress();

	public EMail geteMailAddress();

}
