package de.hawai.bicycle_tracking.server.rest;

        import de.hawai.bicycle_tracking.server.AppConfig;
        import de.hawai.bicycle_tracking.server.DBConfig;
        import de.hawai.bicycle_tracking.server.DBFixuresConfig;
        import de.hawai.bicycle_tracking.server.Main;
        import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
        import de.hawai.bicycle_tracking.server.dto.PasswordDTO;
        import de.hawai.bicycle_tracking.server.dto.TourDTO;
        import de.hawai.bicycle_tracking.server.facade.Facade;
        import de.hawai.bicycle_tracking.server.security.HawaiAuthority;
        import de.hawai.bicycle_tracking.server.security.UserSecurityService;
        import de.hawai.bicycle_tracking.server.utility.test.TestUtil;
        import de.hawai.bicycle_tracking.server.utility.value.Address;
        import de.hawai.bicycle_tracking.server.utility.value.EMail;
        import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;
        import junit.framework.TestCase;
        import org.aspectj.lang.annotation.After;
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
        import org.springframework.test.web.servlet.MvcResult;
        import org.springframework.test.web.servlet.ResultActions;
        import org.springframework.test.web.servlet.ResultHandler;
        import org.springframework.test.web.servlet.setup.MockMvcBuilders;
        import org.springframework.transaction.annotation.Transactional;
        import org.springframework.web.context.WebApplicationContext;

        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.Date;

        import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { Main.class, AppConfig.class, DBConfig.class, DBFixuresConfig.class })
@WebAppConfiguration
@IntegrationTest
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@Transactional(noRollbackFor = Exception.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class TourControllerTest extends TestCase {

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
        facade.createBike("aaa",new FrameNumber(123),new Date(),new Date(), null, facade.getUserBy(new EMail(USER_EMAIL)).get());
    }

    @Test
    public void addRoute_correctStatement_tourIsSaveAndResponseIsOK() throws Exception {
        String testData = "{" +
            "\"name\": \"RouteXYZ\"," +
            "\"bikeID\": 1," +
            "\"lengthInKm\": 123.5," +
            "\"startAt\": \"2015-04-25T12:35:55Z\"," +
            "\"finishedAt\": \"2015-04-25T12:35:55Z\"," +
            "\"waypoints\": [" +
                "{" +
                    "\"latitude\": 53.55705300904082," +
                    "\"longitude\": 10.022841095924377," +
                    "\"name\": \"Hochschule für Angewandte Wissenschaften Campus Berliner Tor, 5-21, " +
                    "Berliner Tor, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}," +
                "{" +
                    "\"latitude\": 53.557301561749455," +
                    "\"longitude\": 10.020703375339508," +
                    "\"name\": \"1, Lübeckertordamm, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}" +
            "]" +
        "}";
        TourDTO request = TestUtil.jsonToObject(TourDTO.class,testData);
        //System.err.println(request);
        ResultActions actions = restViewerMockMvc.perform(post("/api/v1/route")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(testData));
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(200, result.getResponse().getStatus());
                TourDTO response = TestUtil.jsonToObject(TourDTO.class, result.getResponse().getContentAsString());
                assertNotNull(response);
                assertNotNull(response.id);
                assertNotNull(response.bikeID);
                assertNotNull(response.lengthInKm);
                assertNotNull(response.startAt);
                assertNotNull(response.finishedAt);
                assertNotNull(response.createdAt);
                assertNotNull(response.updatedAt);
                assertNotNull(response.waypoints);
                assertEquals(request.bikeID, response.bikeID);
                assertEquals(request.lengthInKm, response.lengthInKm);
                assertEquals(request.startAt, response.startAt);
                assertEquals(request.finishedAt, response.finishedAt);
                assertEquals(request.waypoints, response.waypoints);
            }
        });
    }

    @Test
    public void addRoute_statementNameIsMissing_ResonseWithError400() throws Exception {
        String testData = "{" +
                //"\"name\": \"RouteXYZ\"," +
                "\"bikeID\": 1," +
                "\"lengthInKm\": 123.5," +
                "\"startAt\": \"2015-04-25T12:35:55Z\"," +
                "\"finishedAt\": \"2015-04-25T12:35:55Z\"," +
                "\"waypoints\": [" +
                "{" +
                "\"latitude\": 53.55705300904082," +
                "\"longitude\": 10.022841095924377," +
                "\"name\": \"Hochschule für Angewandte Wissenschaften Campus Berliner Tor, 5-21, " +
                "Berliner Tor, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}," +
                "{" +
                "\"latitude\": 53.557301561749455," +
                "\"longitude\": 10.020703375339508," +
                "\"name\": \"1, Lübeckertordamm, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}" +
                "]" +
                "}";
        TourDTO request = TestUtil.jsonToObject(TourDTO.class,testData);
        ResultActions actions = restViewerMockMvc.perform(post("/api/v1/route")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(testData));
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(400, result.getResponse().getStatus());
            }
        });
    }

    @Test
    public void addRoute_statementBikeIdIsMissing_ResonseWithError400() throws Exception {
        String testData = "{" +
                "\"name\": \"RouteXYZ\"," +
                //"\"bikeID\": 1," +
                "\"lengthInKm\": 123.5," +
                "\"startAt\": \"2015-04-25T12:35:55Z\"," +
                "\"finishedAt\": \"2015-04-25T12:35:55Z\"," +
                "\"waypoints\": [" +
                "{" +
                "\"latitude\": 53.55705300904082," +
                "\"longitude\": 10.022841095924377," +
                "\"name\": \"Hochschule für Angewandte Wissenschaften Campus Berliner Tor, 5-21, " +
                "Berliner Tor, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}," +
                "{" +
                "\"latitude\": 53.557301561749455," +
                "\"longitude\": 10.020703375339508," +
                "\"name\": \"1, Lübeckertordamm, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}" +
                "]" +
                "}";
        TourDTO request = TestUtil.jsonToObject(TourDTO.class,testData);
        ResultActions actions = restViewerMockMvc.perform(post("/api/v1/route")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(testData));
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(400, result.getResponse().getStatus());
            }
        });
    }

    @Test
    public void addRoute_statementLengthInKmIsMissing_ResonseWithError400() throws Exception {
        String testData = "{" +
                "\"name\": \"RouteXYZ\"," +
                "\"bikeID\": 1," +
                //"\"lengthInKm\": 123.5," +
                "\"startAt\": \"2015-04-25T12:35:55Z\"," +
                "\"finishedAt\": \"2015-04-25T12:35:55Z\"," +
                "\"waypoints\": [" +
                "{" +
                "\"latitude\": 53.55705300904082," +
                "\"longitude\": 10.022841095924377," +
                "\"name\": \"Hochschule für Angewandte Wissenschaften Campus Berliner Tor, 5-21, " +
                "Berliner Tor, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}," +
                "{" +
                "\"latitude\": 53.557301561749455," +
                "\"longitude\": 10.020703375339508," +
                "\"name\": \"1, Lübeckertordamm, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}" +
                "]" +
                "}";
        TourDTO request = TestUtil.jsonToObject(TourDTO.class,testData);
        ResultActions actions = restViewerMockMvc.perform(post("/api/v1/route")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(testData));
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(400, result.getResponse().getStatus());
            }
        });
    }

    @Test
    public void addRoute_statementStartAtIsMissing_ResonseWithError400() throws Exception {
        String testData = "{" +
                "\"name\": \"RouteXYZ\"," +
                "\"bikeID\": 1," +
                "\"lengthInKm\": 123.5," +
                //"\"startAt\": \"2015-04-25T12:35:55Z\"," +
                "\"finishedAt\": \"2015-04-25T12:35:55Z\"," +
                "\"waypoints\": [" +
                "{" +
                "\"latitude\": 53.55705300904082," +
                "\"longitude\": 10.022841095924377," +
                "\"name\": \"Hochschule für Angewandte Wissenschaften Campus Berliner Tor, 5-21, " +
                "Berliner Tor, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}," +
                "{" +
                "\"latitude\": 53.557301561749455," +
                "\"longitude\": 10.020703375339508," +
                "\"name\": \"1, Lübeckertordamm, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}" +
                "]" +
                "}";
        TourDTO request = TestUtil.jsonToObject(TourDTO.class,testData);
        ResultActions actions = restViewerMockMvc.perform(post("/api/v1/route")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(testData));
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(400, result.getResponse().getStatus());
            }
        });
    }

    @Test
    public void addRoute_statementFinishedAtIsMissing_ResonseWithError400() throws Exception {
        String testData = "{" +
                "\"name\": \"RouteXYZ\"," +
                "\"bikeID\": 1," +
                "\"lengthInKm\": 123.5," +
                "\"startAt\": \"2015-04-25T12:35:55Z\"," +
                //"\"finishedAt\": \"2015-04-25T12:35:55Z\"," +
                "\"waypoints\": [" +
                "{" +
                "\"latitude\": 53.55705300904082," +
                "\"longitude\": 10.022841095924377," +
                "\"name\": \"Hochschule für Angewandte Wissenschaften Campus Berliner Tor, 5-21, " +
                "Berliner Tor, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}," +
                "{" +
                "\"latitude\": 53.557301561749455," +
                "\"longitude\": 10.020703375339508," +
                "\"name\": \"1, Lübeckertordamm, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}" +
                "]" +
                "}";
        TourDTO request = TestUtil.jsonToObject(TourDTO.class,testData);
        ResultActions actions = restViewerMockMvc.perform(post("/api/v1/route")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(testData));
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(400, result.getResponse().getStatus());
            }
        });
    }

    @Test
    public void addRoute_statementWaypointsIsMissing_ResonseWithError400() throws Exception {
        String testData = "{" +
                "\"name\": \"RouteXYZ\"," +
                "\"bikeID\": 1," +
                "\"lengthInKm\": 123.5," +
                "\"startAt\": \"2015-04-25T12:35:55Z\"," +
                "\"finishedAt\": \"2015-04-25T12:35:55Z\"," +
//                "\"waypoints\": [" +
//                "{" +
//                "\"latitude\": 53.55705300904082," +
//                "\"longitude\": 10.022841095924377," +
//                "\"name\": \"Hochschule für Angewandte Wissenschaften Campus Berliner Tor, 5-21, " +
//                "Berliner Tor, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
//                "}," +
//                "{" +
//                "\"latitude\": 53.557301561749455," +
//                "\"longitude\": 10.020703375339508," +
//                "\"name\": \"1, Lübeckertordamm, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
//                "}" +
//                "]" +
                "}";
        TourDTO request = TestUtil.jsonToObject(TourDTO.class,testData);
        ResultActions actions = restViewerMockMvc.perform(post("/api/v1/route")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(testData));
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(400, result.getResponse().getStatus());
            }
        });
    }

    @Test
    public void addRoute_statementLatitudeIsMissing_ResonseWithError400() throws Exception {
        String testData = "{" +
                "\"name\": \"RouteXYZ\"," +
                "\"bikeID\": 1," +
                "\"lengthInKm\": 123.5," +
                "\"startAt\": \"2015-04-25T12:35:55Z\"," +
                "\"finishedAt\": \"2015-04-25T12:35:55Z\"," +
                "\"waypoints\": [" +
                "{" +
                //"\"latitude\": 53.55705300904082," +
                "\"longitude\": 10.022841095924377," +
                "\"name\": \"Hochschule für Angewandte Wissenschaften Campus Berliner Tor, 5-21, " +
                "Berliner Tor, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}," +
                "{" +
                "\"latitude\": 53.557301561749455," +
                "\"longitude\": 10.020703375339508," +
                "\"name\": \"1, Lübeckertordamm, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}" +
                "]" +
                "}";
        TourDTO request = TestUtil.jsonToObject(TourDTO.class,testData);
        ResultActions actions = restViewerMockMvc.perform(post("/api/v1/route")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(testData));
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(400, result.getResponse().getStatus());
            }
        });
    }

    @Test
    public void addRoute_statementLongitudeIsMissing_ResonseWithError400() throws Exception {
        String testData = "{" +
                "\"name\": \"RouteXYZ\"," +
                "\"bikeID\": 1," +
                "\"lengthInKm\": 123.5," +
                "\"startAt\": \"2015-04-25T12:35:55Z\"," +
                "\"finishedAt\": \"2015-04-25T12:35:55Z\"," +
                "\"waypoints\": [" +
                "{" +
                "\"latitude\": 53.55705300904082," +
                //"\"longitude\": 10.022841095924377," +
                "\"name\": \"Hochschule für Angewandte Wissenschaften Campus Berliner Tor, 5-21, " +
                "Berliner Tor, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}," +
                "{" +
                "\"latitude\": 53.557301561749455," +
                "\"longitude\": 10.020703375339508," +
                "\"name\": \"1, Lübeckertordamm, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}" +
                "]" +
                "}";
        TourDTO request = TestUtil.jsonToObject(TourDTO.class,testData);
        ResultActions actions = restViewerMockMvc.perform(post("/api/v1/route")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(testData));
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(400, result.getResponse().getStatus());
            }
        });
    }

    @Test
    public void addRoute_statementGpsNameIsMissing_ResonseWithError400() throws Exception {
        String testData = "{" +
                "\"name\": \"RouteXYZ\"," +
                "\"bikeID\": 1," +
                "\"lengthInKm\": 123.5," +
                "\"startAt\": \"2015-04-25T12:35:55Z\"," +
                "\"finishedAt\": \"2015-04-25T12:35:55Z\"," +
                "\"waypoints\": [" +
                "{" +
                "\"latitude\": 53.55705300904082," +
                "\"longitude\": 10.022841095924377," +
                //"\"name\": \"Hochschule für Angewandte Wissenschaften Campus Berliner Tor, 5-21, " +
                //"Berliner Tor, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}," +
                "{" +
                "\"latitude\": 53.557301561749455," +
                "\"longitude\": 10.020703375339508," +
                "\"name\": \"1, Lübeckertordamm, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}" +
                "]" +
                "}";
        TourDTO request = TestUtil.jsonToObject(TourDTO.class,testData);
        ResultActions actions = restViewerMockMvc.perform(post("/api/v1/route")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(testData));
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(400, result.getResponse().getStatus());
            }
        });
    }

    @Test
    public void addRoute_statementLongitudeIsNotInCorrectRange_ResonseWithError400() throws Exception {
        String testData = "{" +
                "\"name\": \"RouteXYZ\"," +
                "\"bikeID\": 1," +
                "\"lengthInKm\": 123.5," +
                "\"startAt\": \"2015-04-25T12:35:55Z\"," +
                "\"finishedAt\": \"2015-04-25T12:35:55Z\"," +
                "\"waypoints\": [" +
                "{" +
                "\"latitude\": 53.55705300904082," +
                "\"longitude\": 510.022841095924377," +
                "\"name\": \"Hochschule für Angewandte Wissenschaften Campus Berliner Tor, 5-21, " +
                "Berliner Tor, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}," +
                "{" +
                "\"latitude\": 53.557301561749455," +
                "\"longitude\": 10.020703375339508," +
                "\"name\": \"1, Lübeckertordamm, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}" +
                "]" +
                "}";
        TourDTO request = TestUtil.jsonToObject(TourDTO.class, testData);
        ResultActions actions = restViewerMockMvc.perform(post("/api/v1/route")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(testData));
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(400, result.getResponse().getStatus());
            }
        });
    }

    @Test
    public void addRoute_statementLatitudeIsNotInCorrectRange_ResonseWithError400() throws Exception {
        String testData = "{" +
                "\"name\": \"RouteXYZ\"," +
                "\"bikeID\": 1," +
                "\"lengthInKm\": 123.5," +
                "\"startAt\": \"2015-04-25T12:35:55Z\"," +
                "\"finishedAt\": \"2015-04-25T12:35:55Z\"," +
                "\"waypoints\": [" +
                "{" +
                "\"latitude\": 553.55705300904082," +
                "\"longitude\": 10.022841095924377," +
                "\"name\": \"Hochschule für Angewandte Wissenschaften Campus Berliner Tor, 5-21, " +
                "Berliner Tor, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}," +
                "{" +
                "\"latitude\": 53.557301561749455," +
                "\"longitude\": 10.020703375339508," +
                "\"name\": \"1, Lübeckertordamm, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}" +
                "]" +
                "}";
        TourDTO request = TestUtil.jsonToObject(TourDTO.class,testData);
        ResultActions actions = restViewerMockMvc.perform(post("/api/v1/route")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(testData));
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(400, result.getResponse().getStatus());
            }
        });
    }

    @Test
    public void addRoute_statementWrongStartAtDateFormat_ResonseWithError400() throws Exception {
        String testData = "{" +
                "\"name\": \"RouteXYZ\"," +
                "\"bikeID\": 1," +
                "\"lengthInKm\": 123.5," +
                "\"startAt\": \"201-04-25T12:35:55Z\"," +
                "\"finishedAt\": \"2015-04-25T12:35:55Z\"," +
                "\"waypoints\": [" +
                "{" +
                "\"latitude\": 553.55705300904082," +
                "\"longitude\": 10.022841095924377," +
                "\"name\": \"Hochschule für Angewandte Wissenschaften Campus Berliner Tor, 5-21, " +
                "Berliner Tor, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}," +
                "{" +
                "\"latitude\": 53.557301561749455," +
                "\"longitude\": 10.020703375339508," +
                "\"name\": \"1, Lübeckertordamm, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}" +
                "]" +
                "}";
        TourDTO request = TestUtil.jsonToObject(TourDTO.class, testData);
        ResultActions actions = restViewerMockMvc.perform(post("/api/v1/route")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(testData));
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(400, result.getResponse().getStatus());
            }
        });
    }

    @Test
    public void addRoute_statementWrongFinishedAtDateFormat_ResonseWithError400() throws Exception {
        String testData = "{" +
                "\"name\": \"RouteXYZ\"," +
                "\"bikeID\": 1," +
                "\"lengthInKm\": 123.5," +
                "\"startAt\": \"2015-04-25T12:35:55Z\"," +
                "\"finishedAt\": \"201-04-25T12:35:55Z\"," +
                "\"waypoints\": [" +
                "{" +
                "\"latitude\": 553.55705300904082," +
                "\"longitude\": 10.022841095924377," +
                "\"name\": \"Hochschule für Angewandte Wissenschaften Campus Berliner Tor, 5-21, " +
                "Berliner Tor, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}," +
                "{" +
                "\"latitude\": 53.557301561749455," +
                "\"longitude\": 10.020703375339508," +
                "\"name\": \"1, Lübeckertordamm, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}" +
                "]" +
                "}";
        TourDTO request = TestUtil.jsonToObject(TourDTO.class,testData);
        ResultActions actions = restViewerMockMvc.perform(post("/api/v1/route")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(testData));
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(400, result.getResponse().getStatus());
            }
        });
    }
    @Test
    public void addRoute_statementWrongBikeIdDoesntMatchWithABike_ResonseWithError404() throws Exception {
        String testData = "{" +
                "\"name\": \"RouteXYZ\"," +
                "\"bikeID\": 5," +
                "\"lengthInKm\": 123.5," +
                "\"startAt\": \"2015-04-25T12:35:55Z\"," +
                "\"finishedAt\": \"201-04-25T12:35:55Z\"," +
                "\"waypoints\": [" +
                "{" +
                "\"latitude\": 53.55705300904082," +
                "\"longitude\": 10.022841095924377," +
                "\"name\": \"Hochschule für Angewandte Wissenschaften Campus Berliner Tor, 5-21, " +
                "Berliner Tor, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}," +
                "{" +
                "\"latitude\": 53.557301561749455," +
                "\"longitude\": 10.020703375339508," +
                "\"name\": \"1, Lübeckertordamm, St. Georg, Hamburg-Mitte, Hamburg, 20099, Deutschland\"" +
                "}" +
                "]" +
                "}";
        TourDTO request = TestUtil.jsonToObject(TourDTO.class,testData);
        ResultActions actions = restViewerMockMvc.perform(post("/api/v1/route")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(testData));
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(404, result.getResponse().getStatus());
            }
        });
    }
}

