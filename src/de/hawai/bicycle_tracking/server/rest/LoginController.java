package de.hawai.bicycle_tracking.server.rest;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.Application;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.ApplicationDao;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.LoginSession;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.LoginSessionDao;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.User;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.UserDao;
import de.hawai.bicycle_tracking.server.dto.LoginDTO;
import de.hawai.bicycle_tracking.server.rest.exceptions.InvalidClientException;
import de.hawai.bicycle_tracking.server.rest.exceptions.MalformedRequestException;
import de.hawai.bicycle_tracking.server.rest.exceptions.NotAuthorizedException;
import de.hawai.bicycle_tracking.server.rest.exceptions.NotFoundException;
import de.hawai.bicycle_tracking.server.utility.value.EMail;

@RequestMapping("/api")
@RestController
public class LoginController {
	public enum GrantType {
		PASSWORD;

		@Override
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
		User toLogin = this.userRepository.getByeMailAddress(new EMail(inLoginDTO.getEmail())).get();
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
