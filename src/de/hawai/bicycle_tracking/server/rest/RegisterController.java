package de.hawai.bicycle_tracking.server.rest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hawai.bicycle_tracking.server.security.HawaiAuthority;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.dto.RegistrationDTO;
import de.hawai.bicycle_tracking.server.facade.Facade;
import de.hawai.bicycle_tracking.server.rest.exceptions.AlreadyExistsException;

@RestController
@RequestMapping("/api")
public class RegisterController {
	private final DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	private Facade facade;

	@RequestMapping(value = "/v1/register", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public RegisterResponseV1 registerV1(@RequestBody RegistrationDTO inRegistration)
			throws ParseException {
		IUser newUser = null;
		Date birthdate = null;
		if (inRegistration.getBirthday() != null && inRegistration.getBirthday().length() > 0) {
			birthdate = this.mDateFormat.parse(inRegistration.getBirthday());
		}
		try	{
			newUser = facade.registerUser(inRegistration.getName(),
					inRegistration.getFirstname(),
					inRegistration.getEmail(),
					inRegistration.getAddress(),
					birthdate,
					inRegistration.getPassword(),
					HawaiAuthority.USER);
		} catch (DataIntegrityViolationException e) {
			throw new AlreadyExistsException("User already exists");
		}

		RegisterResponseV1 responseV1 = new RegisterResponseV1();
		responseV1.setEmail(newUser.getMailAddress().getMailAddress());
		responseV1.setToken("");
		return responseV1;
	}

	private static class RegisterResponseV1 {
		private String email;
		private String token;

		@SuppressWarnings("unused")
		public String getEmail() {
			return email;
		}

		public void setEmail(final String inEmail) {
			email = inEmail;
		}

		@SuppressWarnings("unused")
		public String getToken() {
			return token;
		}

		public void setToken(final String inToken) {
			token = inToken;
		}
	}
}
