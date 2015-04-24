package de.hawai.bicycle_tracking.server.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.dto.LoginDTO;
import de.hawai.bicycle_tracking.server.facade.Facade;
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
	private Facade facade;

	@RequestMapping(value = "/v1/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public LoginResponseV1 loginV1(@RequestBody LoginDTO inLoginDTO) {

		if(inLoginDTO.getGrantType().equals(GrantType.PASSWORD.toString()))	{
			return loginPasswordV1(inLoginDTO);
		} else {
			throw new MalformedRequestException("Invalid Grant-Type");
		}
	}

	private LoginResponseV1 loginPasswordV1(LoginDTO inLoginDTO)
	{
		Optional<IUser> userOptional = this.facade.getUserBy(new EMail(inLoginDTO.getEmail()));

		IUser toLogin;
		if (userOptional.isPresent()) {
			toLogin = userOptional.get();
		} else {
			throw new NotFoundException("No User found");
		}
		if(toLogin.getPassword().equals(inLoginDTO.getCode())) {
			LoginResponseV1 responseV1 = new LoginResponseV1();
			responseV1.setEmail(toLogin.getMailAddress().getMailAddress());
			return responseV1;
		} else {
			throw new NotAuthorizedException("Invalid password");
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
