package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import javax.persistence.*;
import de.hawai.bicycle_tracking.server.utility.value.Address;

@Entity
public class SellingLocation implements ISellingLocation
{
	private Address address;
	private String name;

	public SellingLocation(final Address inAddress, final String inName)
	{
		address = inAddress;
		name = inName;
	}

	@Embedded
	@Column(name = "address")
	@Override
	public Address getAddress()
	{
		return address;
	}

	@Column(name = "name")
	@Override
	public String getName()
	{
		return name;
	}

	private void setAddress(final Address inAddress)
	{
		address = inAddress;
	}

	private void setName(final String inName)
	{
		name = inName;
	}
}
