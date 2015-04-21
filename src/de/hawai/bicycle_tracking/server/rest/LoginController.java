package de.hawai.bicycle_tracking.server.rest;

import java.util.UUID;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.*;
import de.hawai.bicycle_tracking.server.dto.LoginDTO;
import de.hawai.bicycle_tracking.server.rest.exceptions.*;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class LoginController {
	public enum GrantType {
		PASSWORD;

		public String toString() {
			return this.name().toLowerCase();
		}
	}

	@Autowired
	private UserDao userRepository;

	@Autowired
	private LoginSessionDao loginSessionRepository;

	@Autowired
	private ApplicationDao applicationRepository;

	@RequestMapping(value = "/v1/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public LoginResponseV1 loginV1(@RequestBody LoginDTO inLoginDTO, @RequestHeader("Client-ID") String inClientID) {
		Application application;
		application = this.applicationRepository.getByClientID(inClientID);
		if(application == null){
			throw new InvalidClientException("Could not find application for specified ID");
		}

		if(inLoginDTO.getGrantType().equals(GrantType.PASSWORD.toString()))	{
			return loginPasswordV1(inLoginDTO, application);
		} else {
			throw new MalformedRequestException("Invalid Grant-Type");
		}
	}

	private LoginResponseV1 loginPasswordV1(LoginDTO inLoginDTO, Application inApplication)
	{
		User toLogin = this.userRepository.getByeMailAddress(new EMail(inLoginDTO.getEmail()));
		if(toLogin == null) {
			throw new NotFoundException("No User found");
		} else {
			if(toLogin.getPassword().equals(inLoginDTO.getCode())) {
				LoginSession session = new LoginSession();
				session.setApplication(inApplication);
				session.setUser(toLogin);
				session.setToken(UUID.randomUUID().toString());
				this.loginSessionRepository.save(session);

				LoginResponseV1 responseV1 = new LoginResponseV1();
				responseV1.setEmail(toLogin.geteMailAddress().geteMailAddress());
				responseV1.setToken(session.getToken());
				return responseV1;
			} else {
				throw new NotAuthorizedException("Invalid password");
			}
		}
	}

	private static class LoginResponseV1 {
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
