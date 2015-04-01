package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import java.util.Date;

import de.hawai.bicycle_tracking.server.utility.EMail;

public interface IUser {

	public String getName();

	public String getFirstName();

	public Date getBirthdate();

	//	public AdressType getAddress();
	//	
	public EMail geteMailAddress();

}
