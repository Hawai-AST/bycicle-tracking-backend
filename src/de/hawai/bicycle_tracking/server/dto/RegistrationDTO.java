package de.hawai.bicycle_tracking.server.dto;

import de.hawai.bicycle_tracking.server.utility.value.Address;

public class RegistrationDTO
{
	private Address address;
	private String email;
	private int kundennr;
	private String vorname;
	private String nachname;
	private String password;
	private String geschlecht;
	private String geburtstag;

	public Address getAddress()
	{
		return address;
	}

	public void setAddress(final Address inAddress)
	{
		address = inAddress;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(final String inEmail)
	{
		email = inEmail;
	}

	public int getKundennr()
	{
		return kundennr;
	}

	public void setKundennr(final int inKundennr)
	{
		kundennr = inKundennr;
	}

	public String getVorname()
	{
		return vorname;
	}

	public void setVorname(final String inVorname)
	{
		vorname = inVorname;
	}

	public String getNachname()
	{
		return nachname;
	}

	public void setNachname(final String inNachname)
	{
		nachname = inNachname;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(final String inPassword)
	{
		password = inPassword;
	}

	public String getGeschlecht()
	{
		return geschlecht;
	}

	public void setGeschlecht(final String inGeschlecht)
	{
		geschlecht = inGeschlecht;
	}

	public String getGeburtstag()
	{
		return geburtstag;
	}

	public void setGeburtstag(final String inGeburtstag)
	{
		geburtstag = inGeburtstag;
	}

	@Override
	public String toString()
	{
		return "RegistrationDTO{" +
				"address=" + address +
				", email=" + email +
				", kundennr=" + kundennr +
				", vorname='" + vorname + '\'' +
				", nachname='" + nachname + '\'' +
				", password='" + password + '\'' +
				", geschlecht='" + geschlecht + '\'' +
				", geburtstag='" + geburtstag + '\'' +
				'}';
	}
}
