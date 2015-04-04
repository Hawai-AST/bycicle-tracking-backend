package de.hawai.bicycle_tracking.server.rest;

import java.text.*;
import java.util.UUID;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.*;
import de.hawai.bicycle_tracking.server.dto.RegistrationDTO;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RegisterController
{
	private final DateFormat m_dateFormat = new SimpleDateFormat("dd.MM.yyyy");

	@Autowired
	private UserDao userRepository;

	@Autowired
	private LoginSessionDao loginSessionRepository;

	@Autowired
	private ApplicationDao applicationRepository;

	@RequestMapping(value = "/v1/register", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public RegisterResponseV1 registerV1(@RequestBody RegistrationDTO inRegistration, @RequestHeader("Client-ID") String inClientID) throws ParseException
	{
		Application application;
		application = this.applicationRepository.getByClientID(inClientID);
		if(application == null){
			throw new RuntimeException("No Client ID specified");
		}

		User newUser = new User();
		newUser.setAddress(inRegistration.getAddress());
		newUser.setBirthdate(this.m_dateFormat.parse(inRegistration.getGeburtstag()));
		newUser.seteMailAddress(new EMail(inRegistration.getEmail()));
		newUser.setFirstName(inRegistration.getVorname());
		newUser.setPassword(inRegistration.getPassword());
		newUser.setName(inRegistration.getNachname());
		try	{
			userRepository.save(newUser);
		} catch(Exception e) {
			throw new RuntimeException("");
		}

		LoginSession session = new LoginSession();
		session.setApplication(application);
		session.setUser(newUser);
		session.setToken(UUID.randomUUID().toString());
		this.loginSessionRepository.save(session);

		RegisterResponseV1 responseV1 = new RegisterResponseV1();
		responseV1.setEmail(newUser.geteMailAddress().geteMailAddress());
		responseV1.setToken(session.getToken());
		return responseV1;
	}

	private static class RegisterResponseV1
	{
		private String email;
		private String token;

		public String getEmail()
		{
			return email;
		}

		public void setEmail(final String inEmail)
		{
			email = inEmail;
		}

		public String getToken()
		{
			return token;
		}

		public void setToken(final String inToken)
		{
			token = inToken;
		}
	}
}
