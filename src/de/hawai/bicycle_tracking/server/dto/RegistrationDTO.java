package de.hawai.bicycle_tracking.server.dto;

import de.hawai.bicycle_tracking.server.utility.value.Address;

public class RegistrationDTO
{
	private Address address;
	private String email;
	private int customerid;
	private String firstname;
	private String name;
	private String password;
	private String gender;
	private String birthday;

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

	public int getCustomerid()
	{
		return customerid;
	}

	public void setCustomerid(final int inCustomerid)
	{
		customerid = inCustomerid;
	}

	public String getFirstname()
	{
		return firstname;
	}

	public void setFirstname(final String inFirstname)
	{
		firstname = inFirstname;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String inName)
	{
		name = inName;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(final String inPassword)
	{
		password = inPassword;
	}

	public String getGender()
	{
		return gender;
	}

	public void setGender(final String inGender)
	{
		gender = inGender;
	}

	public String getBirthday()
	{
		return birthday;
	}

	public void setBirthday(final String inBirthday)
	{
		birthday = inBirthday;
	}

	@Override
	public String toString()
	{
		return "RegistrationDTO{" +
				"address=" + address +
				", email=" + email +
				", customerid=" + customerid +
				", firstname='" + firstname + '\'' +
				", name='" + name + '\'' +
				", password='" + password + '\'' +
				", gender='" + gender + '\'' +
				", birthday='" + birthday + '\'' +
				'}';
	}
}
