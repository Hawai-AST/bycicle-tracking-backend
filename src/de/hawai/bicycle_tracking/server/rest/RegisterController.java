package de.hawai.bicycle_tracking.server.rest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.Application;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.ApplicationDao;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.LoginSession;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.LoginSessionDao;
import de.hawai.bicycle_tracking.server.dto.RegistrationDTO;
import de.hawai.bicycle_tracking.server.facade.Facade;
import de.hawai.bicycle_tracking.server.rest.exceptions.AlreadyExistsException;
import de.hawai.bicycle_tracking.server.rest.exceptions.InvalidClientException;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api")
public class RegisterController {
	private final DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	private Facade facade;

	@Autowired
	private LoginSessionDao loginSessionRepository;

	@Autowired
	private ApplicationDao applicationRepository;

	@RequestMapping(value = "/v1/register", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public RegisterResponseV1 registerV1(@RequestBody RegistrationDTO inRegistration, @RequestHeader("Client-ID") String inClientID)
			throws ParseException {
		Application application;
		application = this.applicationRepository.getByClientID(inClientID);
		if (application == null) {
			throw new InvalidClientException("No Client ID specified");
		}
		IUser newUser = null;
		try	{
			newUser = facade.registerUser(inRegistration.getName(),
					inRegistration.getFirstname(),
					inRegistration.getEmail(),
					inRegistration.getAddress(),
					this.mDateFormat.parse(inRegistration.getBirthday()),
					inRegistration.getPassword());
		} catch (DataIntegrityViolationException e) {
			throw new AlreadyExistsException("User already exists");
		}

		LoginSession session = new LoginSession();
		session.setApplication(application);
		session.setUser(newUser);
		session.setToken(UUID.randomUUID().toString());
		this.loginSessionRepository.save(session);

		RegisterResponseV1 responseV1 = new RegisterResponseV1();
		responseV1.setEmail(newUser.getMailAddress().getMailAddress());
		responseV1.setToken(session.getToken());
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
