package de.hawai.bicycle_tracking.server.utility.value;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EMail {

	private String eMailAddress;
	private static final String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

	@SuppressWarnings("unused")
	private EMail() {
		super();
	}

	public EMail(String eMailAddress) {
		if 	(!verifyCorrectnessOf(eMailAddress)) {
			throw new IllegalArgumentException(eMailAddress + " is not a valid E-Mail address.");
		}
		this.eMailAddress = eMailAddress;
	}

	private boolean verifyCorrectnessOf(String eMailAddress) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(eMailAddress);
		return matcher.matches();
	}

	@Column(name = "email_address", nullable = false, length=250)
	public String geteMailAddress() {
		return eMailAddress;
	}

	public void seteMailAddress(String eMailAddress) {
		this.eMailAddress = eMailAddress;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((eMailAddress == null) ? 0 : eMailAddress.hashCode());
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
		EMail other = (EMail) obj;
		if (eMailAddress == null) {
			if (other.eMailAddress != null)
				return false;
		} else if (!eMailAddress.equals(other.eMailAddress))
			return false;
		return true;
	}

}
