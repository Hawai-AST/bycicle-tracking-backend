package de.hawai.bicycle_tracking.server.rest;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.User;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.UserDao;
import de.hawai.bicycle_tracking.server.dto.LoginDTO;
import de.hawai.bicycle_tracking.server.rest.exceptions.MalformedRequestException;
import de.hawai.bicycle_tracking.server.rest.exceptions.NotAuthorizedException;
import de.hawai.bicycle_tracking.server.rest.exceptions.NotFoundException;
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
		User toLogin = this.userRepository.getByeMailAddress(new EMail(inLoginDTO.getEmail()));
		if(toLogin == null) {
			throw new NotFoundException("No User found");
		} else {
			if(toLogin.getPassword().equals(inLoginDTO.getCode())) {
				LoginResponseV1 responseV1 = new LoginResponseV1();
				responseV1.setEmail(toLogin.geteMailAddress().geteMailAddress());
				responseV1.setToken("");
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
