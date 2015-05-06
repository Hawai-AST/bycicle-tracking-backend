package de.hawai.bicycle_tracking.server.rest;

import de.hawai.bicycle_tracking.server.AppConfig;
import de.hawai.bicycle_tracking.server.DBConfig;
import de.hawai.bicycle_tracking.server.DBFixuresConfig;
import de.hawai.bicycle_tracking.server.Main;
import de.hawai.bicycle_tracking.server.dto.PasswordDTO;
import de.hawai.bicycle_tracking.server.dto.UserDTO;
import de.hawai.bicycle_tracking.server.facade.Facade;
import de.hawai.bicycle_tracking.server.security.HawaiAuthority;
import de.hawai.bicycle_tracking.server.security.UserSecurityService;
import de.hawai.bicycle_tracking.server.utility.test.TestUtil;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Main.class, AppConfig.class, DBConfig.class, DBFixuresConfig.class })
@WebAppConfiguration
@IntegrationTest
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(noRollbackFor = Exception.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class AccountControllerTest {
    private static final String USER_EMAIL = "peter@buttington.com";

    private static final String NEW_PASSWORD = "butts";
    private static final String NEW_NAME = "Poopy";
    private static final String NEW_LAST_NAME = "McPoopster";
    private static final String NEW_BIRTHDAY = "1999-01-01";
    private static final Address NEW_ADDRESS = new Address("bb", "dd", "aa", "zz", "dd", "awwsd");

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserSecurityService authenticationService;

    @Autowired
    private Facade facade;

    private MockMvc restViewerMockMvc;

    private UserDetails user;

    @Before
    public void setup() {
        this.restViewerMockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        facade.registerUser("Buttington", "Peter", new EMail(USER_EMAIL), new Address("aa", "ds", "asd", "asd", "asd", "asd"),
                new Date(1), "poop", HawaiAuthority.USER);

        this.user = authenticationService.loadUserByUsername(USER_EMAIL);
    }

    @Test
    public void setPassword_WithCode_Success() throws Exception {
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setCode(NEW_PASSWORD);
        this.restViewerMockMvc.perform(post("/api/v1/user/password")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(TestUtil.convertObjectToJsonBytes(passwordDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void getUser_WithUser_Success() throws Exception {
        this.restViewerMockMvc.perform(get("/api/v1/user")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user)))
                .andExpect(status().isOk()).andExpect(content().string(containsString("firstName")));
    }

    @Test
    public void updateUser_ValidInfor_Success() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setAddress(NEW_ADDRESS);
        userDTO.setBirthdate(NEW_BIRTHDAY);
        userDTO.setFirstName(NEW_NAME);
        userDTO.setName(NEW_LAST_NAME);
        this.restViewerMockMvc.perform(post("/api/v1/user")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(TestUtil.convertObjectToJsonBytes(userDTO)))
                .andExpect(status().isOk());
    }
}
