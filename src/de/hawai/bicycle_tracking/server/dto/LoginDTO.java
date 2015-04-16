package de.hawai.bicycle_tracking.server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginDTO {
	@JsonProperty("grant-type")
	private String grantType;
	private String email;
	private String code;

	public String getEmail() {
		return email;
	}

	public void setEmail(final String inEmail) {
		email = inEmail;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String inCode) {
		code = inCode;
	}

	public String getGrantType() {
		return grantType;
	}

	public void setGrantType(final String inGrantType) {
		grantType = inGrantType;
	}

	@Override
	public String toString() {
		return "LoginDTO{" +
				"grantType='" + grantType + '\'' +
				", email='" + email + '\'' +
				", code='" + code + '\'' +
				'}';
	}
}
