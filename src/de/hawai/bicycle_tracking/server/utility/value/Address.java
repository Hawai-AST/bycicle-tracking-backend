package de.hawai.bicycle_tracking.server.utility.value;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {

	private String street;
	private String city;
	private String postcode;
	private String state;

	@SuppressWarnings("unused")
	private Address() {
		super();
	}

	public Address(String street, String city, String state, String postcode) {
		this.street = street;
		this.city = city;
		this.state = state;
		this.postcode = postcode;

	}

	@Column(name = "ADDRESS_STREET", nullable = false, length=250)
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@Column(name = "ADDRESS_CITY", nullable = false, length=50)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "ADDRESS_STATE", nullable = false, length=50)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "ADDRESS_POSTCODE", nullable = false, length=10)
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
}
