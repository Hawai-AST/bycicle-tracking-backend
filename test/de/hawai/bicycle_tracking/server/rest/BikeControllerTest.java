package de.hawai.bicycle_tracking.server.rest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.joda.time.Period;
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

import de.hawai.bicycle_tracking.server.AppConfig;
import de.hawai.bicycle_tracking.server.DBConfig;
import de.hawai.bicycle_tracking.server.DBFixuresConfig;
import de.hawai.bicycle_tracking.server.Main;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.BikeType;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBikeTypeDao;
import de.hawai.bicycle_tracking.server.dto.BikeDTO;
import de.hawai.bicycle_tracking.server.facade.Facade;
import de.hawai.bicycle_tracking.server.security.HawaiAuthority;
import de.hawai.bicycle_tracking.server.security.UserSecurityService;
import de.hawai.bicycle_tracking.server.utility.test.TestUtil;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Main.class, AppConfig.class, DBConfig.class, DBFixuresConfig.class })
@WebAppConfiguration
@IntegrationTest
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(noRollbackFor = Exception.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class BikeControllerTest {
    private static final String USER_EMAIL = "peter@buttingtome.com";
    private static final String OTHER_USER_EMAIL = "peter2@buttingtome.com";

    private static final String BIKE_NAME = "mae byke";
    private static final int BIKE_FRAME_NUMBER = 1;
    private BikeType bikeType;
    private static final String BIKE_MAINTENANCE = "1999-01-01";
    private static final String BIKE_PURCHASE_DATE = "1990-01-01";

    private BikeType bikeNewType;
    private static final int BIKE_NEW_FRAME_NUMBER = 2;

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserSecurityService authenticationService;
    
    @Autowired
    private IBikeTypeDao bikeTypeRepository;

    @Autowired
    private Facade facade;

    private MockMvc restViewerMockMvc;

    private BikeDTO bike;

    private UserDetails user;

    @Before
    public void setup() {
        this.restViewerMockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        facade.registerUser("Buttington", "Peter", new EMail(USER_EMAIL), new Address("aa", "ds", "asd", "asd", "asd", "asd"),
                new Date(1), "poop", HawaiAuthority.USER);

        facade.registerUser("Buttington", "Peter2", new EMail(OTHER_USER_EMAIL), new Address("aa", "ds", "asd", "asd", "asd", "asd"),
                new Date(1), "poop", HawaiAuthority.USER);
        
        bikeType = bikeTypeRepository.save(new BikeType("City Bike", "for the city", Period.weeks(4)));
        bikeNewType = bikeTypeRepository.save(new BikeType("City Cross", "for the cross", Period.weeks(2)));

        System.err.println(bikeType + "\n");
        this.bike = new BikeDTO();
        this.bike.setFrameNumber(BIKE_FRAME_NUMBER);
        this.bike.setNextMaintenance(BIKE_MAINTENANCE);
        this.bike.setPurchaseDate(BIKE_PURCHASE_DATE);
        this.bike.setType(bikeType);
        
        System.err.println(this.bike.getType() + "\n");
        this.user = authenticationService.loadUserByUsername(USER_EMAIL);
    }

    @Test
    public void saleslocation_LoggedIn_SuccessfulResult() throws Exception {
        this.restViewerMockMvc.perform(get("/api/v1/saleslocations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user)))
                .andExpect(status().isOk()).andExpect(content().string(containsString("amount")));
    }

    @Test
    public void createBike_LoggedIn_BikeCreated() throws Exception {
        this.restViewerMockMvc.perform(post("/api/v1/bike")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(TestUtil.convertObjectToJsonBytes(bike)))
                .andExpect(status().isOk()).andExpect(content().string(containsString("id")));
    }

    @Test
    public void getBikes_LoggedIn_SuccessfulResult() throws Exception {
        this.restViewerMockMvc.perform(get("/api/v1/bikes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user)))
                .andExpect(status().isOk()).andExpect(content().string(containsString("amount")));
    }

    @Test
    public void updateBike_OwnBike_SuccessfulResult() throws Exception {
        IBike old = facade.createBike(bikeType, new FrameNumber(BIKE_FRAME_NUMBER), dateFormat.parse(BIKE_MAINTENANCE),
                dateFormat.parse(BIKE_PURCHASE_DATE), null, facade.getUserBy(new EMail(USER_EMAIL)).get(), BIKE_NAME);

        UUID id = facade.getIdOfBike(old);
        BikeDTO newBike = new BikeDTO();
        newBike.setFrameNumber(BIKE_NEW_FRAME_NUMBER);
        newBike.setNextMaintenance(BIKE_MAINTENANCE);
        newBike.setPurchaseDate(BIKE_PURCHASE_DATE);
        newBike.setType(bikeNewType);

        this.restViewerMockMvc.perform(post("/api/v1/bike/" + id)
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(TestUtil.convertObjectToJsonBytes(newBike)))
                .andExpect(status().isOk()).andExpect(content().string(containsString("id")));
    }

    @Test
    public void updateBike_InvalidBike_NotFound() throws Exception {
    	// this can break randomly, but probably never will ~~
        this.restViewerMockMvc.perform(post("/api/v1/bike/" + UUID.randomUUID())
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(TestUtil.convertObjectToJsonBytes(bike)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateBike_BikeFromOther_Forbidden() throws Exception {
        IBike old = facade.createBike(bikeType, new FrameNumber(BIKE_FRAME_NUMBER), dateFormat.parse(BIKE_MAINTENANCE),
                dateFormat.parse(BIKE_PURCHASE_DATE), null, facade.getUserBy(new EMail(OTHER_USER_EMAIL)).get(), BIKE_NAME);

        UUID id = facade.getIdOfBike(old);
        BikeDTO newBike = new BikeDTO();
        newBike.setFrameNumber(BIKE_NEW_FRAME_NUMBER);
        newBike.setNextMaintenance(BIKE_MAINTENANCE);
        newBike.setPurchaseDate(BIKE_PURCHASE_DATE);
        newBike.setType(bikeNewType);
        this.restViewerMockMvc.perform(post("/api/v1/bike/" + id)
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(TestUtil.convertObjectToJsonBytes(newBike)))
                .andExpect(status().isForbidden());
    }
}
