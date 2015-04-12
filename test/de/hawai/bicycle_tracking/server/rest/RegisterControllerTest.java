package de.hawai.bicycle_tracking.server.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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

import de.hawai.bicycle_tracking.server.AppConfig;
import de.hawai.bicycle_tracking.server.DBConfig;
import de.hawai.bicycle_tracking.server.DBFixuresConfig;
import de.hawai.bicycle_tracking.server.Main;
import de.hawai.bicycle_tracking.server.TestUtil;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.UserDao;
import de.hawai.bicycle_tracking.server.dto.RegistrationDTO;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {  Main.class, AppConfig.class, DBConfig.class, DBFixuresConfig.class })
@WebAppConfiguration
@IntegrationTest
public class RegisterControllerTest {
	private static final String NAME = "Hans";
	private static final String LASTNAME = "Wurst";

	private static final EMail EMAIL = new EMail("hans@wurst.com");
	private static final String BIRTHDATE = "1970-01-01";
	private static final String INVALID_BIRTHDATE = "Erster.Erster.Siebzig";
	private static final String PASSWORD = "thisismypassword";
	private static final int CUSTOMERNR = 1;
	private static final int INVALID_CUSTOMERNR = 1337;
	private static final String GENDER = "male";
	private static final String STREET = "Wurstalee";
	private static final String HOUSENR = "1";
	private static final String CITY = "Bielefeld";
	private static final String POSTCODE = "42042";
	private static final String STATE = "NIE";
	private static final String COUNTRY = "Germany";
	private static final Address ADDRESS;

	private MockMvc restViewerMockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private UserDao userRepository;

	private RegistrationDTO registration;

	static {
		ADDRESS = new Address(STREET, HOUSENR, CITY, STATE, POSTCODE, COUNTRY);
	}

	@Before
	public void setup() {
		this.restViewerMockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		this.registration = new RegistrationDTO();
		this.registration.setAddress(ADDRESS);
		this.registration.setEmail(EMAIL);
		this.registration.setBirthday(BIRTHDATE);
		this.registration.setGender(GENDER);
		this.registration.setCustomerid(CUSTOMERNR);
		this.registration.setName(LASTNAME);
		this.registration.setFirstname(NAME);
		this.registration.setPassword(PASSWORD);
	}

	@After
	public void teardown() {
		this.userRepository.deleteAll();
	}

	@Test
	public void testRegister() throws Exception {
		assertThat(this.userRepository.findAll()).hasSize(0);
		this.restViewerMockMvc.perform(post("/api/v1/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(registration))
				.header("Client-ID", "DEV-101")).andExpect(status().isOk());
		assertThat(this.userRepository.findAll()).hasSize(1);
	}

	@Test
	public void testDoubleRegister() throws Exception {
		this.restViewerMockMvc.perform(post("/api/v1/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(registration))
				.header("Client-ID", "DEV-101")).andExpect(status().isOk());
		this.restViewerMockMvc.perform(post("/api/v1/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(registration))
				.header("Client-ID", "DEV-101")).andExpect(status().isConflict());
	}

	@Test
	public void testInvalidInput() throws Exception {
		this.registration.setBirthday(INVALID_BIRTHDATE);
		this.restViewerMockMvc.perform(post("/api/v1/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(registration))
				.header("Client-ID", "DEV-101")).andExpect(status().isBadRequest());
	}

	@Test
	@Ignore // Skip because we don't have the backend for customer numbers yet.
	public void testInvalidCustomerNr() throws Exception {
		this.registration.setCustomerid(INVALID_CUSTOMERNR);
		this.restViewerMockMvc.perform(post("/api/v1/register").contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(registration))
				.header("Client-ID", "DEV-101")).andExpect(status().isNotFound());
	}
}
