package de.hawai.bicycle_tracking.server.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Base64;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import de.hawai.bicycle_tracking.server.AppConfig;
import de.hawai.bicycle_tracking.server.DBConfig;
import de.hawai.bicycle_tracking.server.DBFixuresConfig;
import de.hawai.bicycle_tracking.server.Main;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUserDao;
import de.hawai.bicycle_tracking.server.facade.Facade;
import de.hawai.bicycle_tracking.server.security.HawaiAuthority;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {  Main.class, AppConfig.class, DBConfig.class, DBFixuresConfig.class })
@WebAppConfiguration
@IntegrationTest
public class SecurityTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    @Resource(name = "IUserDao")
    private IUserDao userRepository;
    
    @Autowired
    private Facade facade;

    private MockMvc restViewerMockMvc;

    @Before
    public void setup() {
        this.restViewerMockMvc = MockMvcBuilders.webAppContextSetup(context).addFilter(springSecurityFilterChain).build();
        this.userRepository.deleteAll();
        facade.registerUser("Buttington", "Peter", new EMail("peter@buttington.com"), new Address("aa", "ds", "asd", "asd", "asd", "asd"),
                new Date(1), "poop", HawaiAuthority.ADMIN);
        facade.registerUser("Buttington", "Peter", new EMail("peter2@buttington.com"), new Address("aa", "ds", "asd", "asd", "asd", "asd"),
                new Date(1), "poop", HawaiAuthority.USER);
    }

    @Test
    public void accessAdmin_WithoutLogin_Unauthorized() throws Exception {
        this.restViewerMockMvc.perform(get("/api/v1/admin")).andExpect(status().isUnauthorized());
    }

    private String setup_access_token(String username, String password) throws Exception {
        String authorization = "Basic " + new String(Base64.getEncoder().encode("DEV-101:DEVSECRET".getBytes()));

        String content = restViewerMockMvc
                .perform(post("/oauth/token").header("Authorization", authorization)
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("username", username).param("password", password)
                                .param("grant_type", "password").param("scope", "read write"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return content.substring(17, 53);
    }

    @Test
    public void accessAdmin_AsAdmin_Success() throws Exception {
        String token = setup_access_token("peter@buttington.com", "poop");
        this.restViewerMockMvc.perform(get("/api/v1/admin").header("Authorization", "Bearer " + token)).andExpect(status().isOk());
    }

    @Test
    public void accessAdmin_AsUser_Forbidden() throws Exception {
        String token2 = setup_access_token("peter2@buttington.com", "poop");
        this.restViewerMockMvc.perform(get("/api/v1/admin").header("Authorization", "Bearer " + token2)).andExpect(status().isForbidden());
    }
}
