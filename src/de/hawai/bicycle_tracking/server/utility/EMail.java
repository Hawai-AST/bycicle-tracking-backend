package de.hawai.bicycle_tracking.server.utility;

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

}
