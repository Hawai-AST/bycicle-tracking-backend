package de.hawai.bicycle_tracking.server.rest;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.User;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.UserDao;
import de.hawai.bicycle_tracking.server.dto.RegistrationDTO;
import de.hawai.bicycle_tracking.server.rest.exceptions.AlreadyExistsException;
import de.hawai.bicycle_tracking.server.security.HawaiAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/api")
public class RegisterController
{
	private final DateFormat m_dateFormat = new SimpleDateFormat("dd.MM.yyyy");

	@Autowired
	private UserDao userRepository;

	@RequestMapping(value = "/v1/register", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public RegisterResponseV1 registerV1(@RequestBody RegistrationDTO inRegistration, @RequestHeader("Client-ID") String inClientID) throws ParseException
	{
		User newUser = new User();
		newUser.setAddress(inRegistration.getAddress());
		newUser.setBirthdate(this.m_dateFormat.parse(inRegistration.getBirthday()));
		newUser.seteMailAddress(inRegistration.getEmail());
		newUser.setFirstName(inRegistration.getFirstname());
		newUser.setPassword(inRegistration.getPassword());
		newUser.setName(inRegistration.getName());
		newUser.setAuthority(HawaiAuthority.USER);
		try	{
			userRepository.save(newUser);
		} catch(DataIntegrityViolationException e) {
			throw new AlreadyExistsException("User already exists");
		}

		RegisterResponseV1 responseV1 = new RegisterResponseV1();
		responseV1.setEmail(newUser.geteMailAddress().geteMailAddress());
		responseV1.setToken("");
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
