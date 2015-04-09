package de.hawai.bicycle_tracking.server.utility.value;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@SuppressWarnings("unused")
public class Address {

	private String street;
	private String houseNumber;
	private String city;
	private String state;
	private String postcode;
	private String country;

	private Address() {
		super();
	}

	public Address(String street, String houseNumber, String city, String state, String postcode, String country) {
		this.street = street;
		this.houseNumber = houseNumber;
		this.city = city;
		this.state = state;
		this.postcode = postcode;
		this.country = country;

	}

	@Column(name = "address_street", nullable = false, length=250)
	public String getStreet() {
		return street;
	}

	private void setStreet(String street) {
		this.street = street;
	}

	@Column(name = "address_city", nullable = false, length=50)
	public String getCity() {
		return city;
	}

	private void setCity(String city) {
		this.city = city;
	}

	@Column(name = "address_state", nullable = false, length=50)
	public String getState() {
		return state;
	}

	private void setState(String state) {
		this.state = state;
	}

	@Column(name = "address_postcode", nullable = false, length = 10)
	public String getPostcode() {
		return postcode;
	}

	private void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	@Column(name = "address_house_number", nullable = false, length = 10)
	public String getHouseNumber() {
		return houseNumber;
	}

	private void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	@Column(name = "address_country", nullable = false, length = 30)
	public String getCountry() {
		return country;
	}

	private void setCountry(String country) {
		this.country = country;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result
				+ ((houseNumber == null) ? 0 : houseNumber.hashCode());
		result = prime * result
				+ ((postcode == null) ? 0 : postcode.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (houseNumber == null) {
			if (other.houseNumber != null)
				return false;
		} else if (!houseNumber.equals(other.houseNumber))
			return false;
		if (postcode == null) {
			if (other.postcode != null)
				return false;
		} else if (!postcode.equals(other.postcode))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		return true;
	}
}
