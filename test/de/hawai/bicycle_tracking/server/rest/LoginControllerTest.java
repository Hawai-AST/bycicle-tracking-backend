package de.hawai.bicycle_tracking.server.rest;

import de.hawai.bicycle_tracking.server.*;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.User;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.UserDao;
import de.hawai.bicycle_tracking.server.dto.LoginDTO;
import de.hawai.bicycle_tracking.server.security.HawaiAuthority;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Main.class, AppConfig.class, DBConfig.class, DBFixuresConfig.class })
@WebAppConfiguration
@IntegrationTest
public class LoginControllerTest
{
	private static final String EMAIL = "hans@wurst.com";
	private static final String INVALID_EMAIL = "hans@peter.com";
	private static final String PASSWORD = "thisismypassword";
	private static final String INVALID_PASSWORD = "thisisnotmypassword";
	private static final String NAME = "Hans";
	private static final String LASTNAME = "Wurst";
	private static Date BIRTHDATE;
	private static final int CUSTOMERNR = 1;
	private static final String GENDER = "male";
	private static final String STREET = "Wurstalee";
	private static final String HOUSENR = "1";
	private static final String CITY = "Bielefeld";
	private static final String POSTCODE = "42042";
	private static final String STATE = "NIE";
	private static final String COUNTRY = "Germany";

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private UserDao userRepository;

	private MockMvc restViewerMockMvc;

	private LoginDTO login;

	@PostConstruct
	public void setup() throws ParseException
	{
		BIRTHDATE = new SimpleDateFormat("dd.MM.yyyy").parse("01.01.1970");
		this.restViewerMockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Before
	public void initTest() {
		User user = new User();
		Address address = new Address(STREET, HOUSENR, CITY, STATE, POSTCODE, COUNTRY);
		user.setAddress(address);
		user.setFirstName(NAME);
		user.setName(LASTNAME);
		user.setPassword(PASSWORD);
		user.setBirthdate(BIRTHDATE);
		user.seteMailAddress(new EMail(EMAIL));
		user.setAuthority(HawaiAuthority.USER);
		userRepository.save(user);

		this.login = new LoginDTO();
		this.login.setEmail(EMAIL);
		this.login.setCode(PASSWORD);
		this.login.setGrantType("password");
	}

	@After
	public void teardown()
	{
		userRepository.deleteAll();
	}

	@Test
	public void testLogin() throws Exception
	{
		this.restViewerMockMvc.perform(post("/api/v1/login").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(this.login)).header("Client-ID", "DEV-101")).andExpect(status().isOk());
	}

	@Test
	public void testInvalidPassword() throws Exception
	{
		this.login.setCode(INVALID_PASSWORD);
		this.restViewerMockMvc.perform(post("/api/v1/login").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(this.login)).header("Client-ID", "DEV-101")).andExpect(status().isUnauthorized());
	}

	@Test
	public void testInvalidUser() throws Exception
	{
		this.login.setEmail(INVALID_EMAIL);
		this.restViewerMockMvc.perform(post("/api/v1/login").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(this.login)).header("Client-ID", "DEV-101")).andExpect(status().isNotFound());
	}
}
