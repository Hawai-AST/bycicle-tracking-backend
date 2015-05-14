package de.hawai.bicycle_tracking.server.rest;

import de.hawai.bicycle_tracking.server.AppConfig;
        import de.hawai.bicycle_tracking.server.DBConfig;
        import de.hawai.bicycle_tracking.server.DBFixuresConfig;
        import de.hawai.bicycle_tracking.server.Main;
        import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.astcore.tourmanagement.AddTourFailedException;
import de.hawai.bicycle_tracking.server.astcore.tourmanagement.ITour;
import de.hawai.bicycle_tracking.server.dto.TourDTO;
import de.hawai.bicycle_tracking.server.dto.TourListEntryDTO;
import de.hawai.bicycle_tracking.server.facade.Facade;
        import de.hawai.bicycle_tracking.server.security.HawaiAuthority;
        import de.hawai.bicycle_tracking.server.security.UserSecurityService;
        import de.hawai.bicycle_tracking.server.utility.test.TestUtil;
        import de.hawai.bicycle_tracking.server.utility.value.Address;
        import de.hawai.bicycle_tracking.server.utility.value.EMail;
        import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;
import de.hawai.bicycle_tracking.server.utility.value.GPS;
import junit.framework.TestCase;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static de.hawai.bicycle_tracking.server.utility.test.TestUtil.jsonToObject;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional(noRollbackFor = Exception.class)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@SpringApplicationConfiguration(classes = { Main.class, AppConfig.class, DBConfig.class, DBFixuresConfig.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@WebAppConfiguration
@IntegrationTest

public class TourControllerTest extends TestCase {

    private static final String USER_EMAIL = "peter@buttington.com";

    private static final String NEW_PASSWORD = "butts";
    private static final String NEW_NAME = "Poopy";
    private static final String NEW_LAST_NAME = "McPoopster";
    private static final String NEW_BIRTHDAY = "1999-01-01";
    private static final Address NEW_ADDRESS = new Address("bb", "dd", "aa", "zz", "dd", "awwsd");

    private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserSecurityService authenticationService;

    @Autowired
    private Facade facade;

    private MockMvc restViewerMockMvc;

    private UserDetails user;
    private IBike bike;

    @Before
    public void setup() {
        this.restViewerMockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        facade.registerUser("Buttington", "Peter", new EMail(USER_EMAIL), new Address("aa", "ds", "asd", "asd", "asd", "asd"),
                new Date(1), "poop", HawaiAuthority.USER);

        this.user = authenticationService.loadUserByUsername(USER_EMAIL);
        bike = facade.createBike("aaa", new FrameNumber(123), new Date(), new Date(), null, facade.getUserBy(new EMail(USER_EMAIL)).get());
    }

    @Test
    public void addRoute_correctStatement_tourIsSaveAndResponseIsOK() throws Exception {
        String testData = "{" +
            "\"name\": \"RouteXYZ\"," +
            "\"bikeID\": " + bike.getId() + "," +
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
        ResultActions actions = restViewerMockMvc.perform(post("/api/v1/route")
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(testData));
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(200, result.getResponse().getStatus());
                TourDTO response = jsonToObject(TourDTO.class, result.getResponse().getContentAsString());
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
    public void addRoute_statementNameIsMissing_ResponseWithError400() throws Exception {
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
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
    public void addRoute_statementBikeIdIsMissing_ResponseWithError400() throws Exception {
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
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
    public void addRoute_statementLengthInKmIsMissing_ResponseWithError400() throws Exception {
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
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
    public void addRoute_statementStartAtIsMissing_ResponseWithError400() throws Exception {
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
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
    public void addRoute_statementFinishedAtIsMissing_ResponseWithError400() throws Exception {
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
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
    public void addRoute_statementWaypointsIsMissing_ResponseWithError400() throws Exception {
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
    public void addRoute_statementLatitudeIsMissing_ResponseWithError400() throws Exception {
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
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
    public void addRoute_statementLongitudeIsMissing_ResponseWithError400() throws Exception {
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
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
    public void addRoute_statementGpsNameIsMissing_ResponseWithError400() throws Exception {
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
    public void addRoute_statementLongitudeIsNotInCorrectRange_ResponseWithError400() throws Exception {
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
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
    public void addRoute_statementLatitudeIsNotInCorrectRange_ResponseWithError400() throws Exception {
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
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
    public void addRoute_statementWrongStartAtDateFormat_ResponseWithError400() throws Exception {
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
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
    public void addRoute_statementWrongFinishedAtDateFormat_ResponseWithError400() throws Exception {
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
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
    public void addRoute_statementWrongBikeIdDoesntMatchWithABike_ResponseWithError404() throws Exception {
        String testData = "{" +
                "\"name\": \"RouteXYZ\"," +
                "\"bikeID\": 50000," +
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
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

    @Test
    public void changeRoute_correctStatement_tourIsSaveAndResponseIsOK() throws Exception {
        ITour tour = facade.addTour(
                "TestTour",
                bike,
                new Date(),
                new Date(),
                new ArrayList<GPS>(),
                2
        );
        String testData = "{" +
                "\"name\": \"RouteXYZ\"," +
                "\"bikeID\": " + bike.getId() + "," +
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
        ResultActions actions = restViewerMockMvc.perform(put("/api/v1/route/" + tour.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(testData));
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(200, result.getResponse().getStatus());
                TourDTO response = jsonToObject(TourDTO.class, result.getResponse().getContentAsString());
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
    public void changeRoute_correctStatementButUserDoesNotOwnTheRoute_ErrorNotAuthorized() throws Exception {
        IUser user2 = facade.registerUser(
                "testUser2",
                "dsfdsf",
                new EMail("test@test432.de"),
                new Address(
                        "lkdsfsdfjösd",
                        "11",
                        "HH",
                        "SH",
                        "21521",
                        "DE"
                ),
                new Date(),
                "TestPw",
                HawaiAuthority.USER
        );
        UserDetails user2Details = authenticationService.loadUserByUsername("test@test432.de");
        ITour tour = facade.addTour(
                "TestTour",
                bike,
                new Date(),
                new Date(),
                new ArrayList<GPS>(),
                2
        );
        String testData = "{" +
                "\"name\": \"RouteXYZ\"," +
                "\"bikeID\": " + bike.getId() + "," +
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
        ResultActions actions = restViewerMockMvc.perform(put("/api/v1/route/" + tour.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user2Details))
                .content(testData)).andExpect(status().isUnauthorized());
    }

    @Test
    public void changeRoute_statementNameIsMissing_ResponseWithError400() throws Exception {
        ITour tour = facade.addTour(
                "TestTour",
                bike,
                new Date(),
                new Date(),
                new ArrayList<GPS>(),
                2
        );
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
        ResultActions actions = restViewerMockMvc.perform(put("/api/v1/route/" + tour.getId())
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
    public void changeRoute_statementBikeIdIsMissing_ResponseWithError400() throws Exception {
        ITour tour = facade.addTour(
                "TestTour",
                bike,
                new Date(),
                new Date(),
                new ArrayList<GPS>(),
                2
        );
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
        ResultActions actions = restViewerMockMvc.perform(put("/api/v1/route/" + tour.getId())
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
    public void changeRoute_statementLengthInKmIsMissing_ResponseWithError400() throws Exception {
        ITour tour = facade.addTour(
                "TestTour",
                bike,
                new Date(),
                new Date(),
                new ArrayList<GPS>(),
                2
        );
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
        ResultActions actions = restViewerMockMvc.perform(put("/api/v1/route/" + tour.getId())
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
    public void changeRoute_statementStartAtIsMissing_ResponseWithError400() throws Exception {
        ITour tour = facade.addTour(
                "TestTour",
                bike,
                new Date(),
                new Date(),
                new ArrayList<GPS>(),
                2
        );
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
        ResultActions actions = restViewerMockMvc.perform(put("/api/v1/route/" + tour.getId())
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
    public void changeRoute_statementFinishedAtIsMissing_ResponseWithError400() throws Exception {
        ITour tour = facade.addTour(
                "TestTour",
                bike,
                new Date(),
                new Date(),
                new ArrayList<GPS>(),
                2
        );
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
        ResultActions actions = restViewerMockMvc.perform(put("/api/v1/route/" + tour.getId())
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
    public void changeRoute_statementWaypointsIsMissing_ResponseWithError400() throws Exception {
        ITour tour = facade.addTour(
                "TestTour",
                bike,
                new Date(),
                new Date(),
                new ArrayList<GPS>(),
                2
        );
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
        ResultActions actions = restViewerMockMvc.perform(put("/api/v1/route/" + tour.getId())
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
    public void changeRoute_statementLatitudeIsMissing_ResponseWithError400() throws Exception {
        ITour tour = facade.addTour(
                "TestTour",
                bike,
                new Date(),
                new Date(),
                new ArrayList<GPS>(),
                2
        );
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
        ResultActions actions = restViewerMockMvc.perform(put("/api/v1/route/" + tour.getId())
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
    public void changeRoute_statementLongitudeIsMissing_ResponseWithError400() throws Exception {
        ITour tour = facade.addTour(
                "TestTour",
                bike,
                new Date(),
                new Date(),
                new ArrayList<GPS>(),
                2
        );
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
        ResultActions actions = restViewerMockMvc.perform(put("/api/v1/route/" + tour.getId())
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
    public void changeRoute_statementGpsNameIsMissing_ResponseWithError400() throws Exception {
        ITour tour = facade.addTour(
                "TestTour",
                bike,
                new Date(),
                new Date(),
                new ArrayList<GPS>(),
                2
        );
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
        ResultActions actions = restViewerMockMvc.perform(put("/api/v1/route/" + tour.getId())
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
    public void changeRoute_statementLongitudeIsNotInCorrectRange_ResponseWithError400() throws Exception {
        ITour tour = facade.addTour(
                "TestTour",
                bike,
                new Date(),
                new Date(),
                new ArrayList<GPS>(),
                2
        );
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
        ResultActions actions = restViewerMockMvc.perform(put("/api/v1/route/" + tour.getId())
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
    public void changeRoute_statementLatitudeIsNotInCorrectRange_ResponseWithError400() throws Exception {
        ITour tour = facade.addTour(
                "TestTour",
                bike,
                new Date(),
                new Date(),
                new ArrayList<GPS>(),
                2
        );
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
        ResultActions actions = restViewerMockMvc.perform(put("/api/v1/route/" + tour.getId())
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
    public void changeRoute_statementWrongStartAtDateFormat_ResponseWithError400() throws Exception {
        ITour tour = facade.addTour(
                "TestTour",
                bike,
                new Date(),
                new Date(),
                new ArrayList<GPS>(),
                2
        );
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
        ResultActions actions = restViewerMockMvc.perform(put("/api/v1/route/" + tour.getId())
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
    public void changeRoute_statementWrongFinishedAtDateFormat_ResponseWithError400() throws Exception {
        ITour tour = facade.addTour(
                "TestTour",
                bike,
                new Date(),
                new Date(),
                new ArrayList<GPS>(),
                2
        );
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
        ResultActions actions = restViewerMockMvc.perform(put("/api/v1/route/" + tour.getId())
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
    public void changeRoute_statementWrongBikeIdDoesntMatchWithABike_ResponseWithError404() throws Exception {
        ITour tour = facade.addTour(
                "TestTour",
                bike,
                new Date(),
                new Date(),
                new ArrayList<GPS>(),
                2
        );
        String testData = "{" +
                "\"name\": \"RouteXYZ\"," +
                "\"bikeID\": 50000," +
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
        TourDTO request = jsonToObject(TourDTO.class, testData);
        ResultActions actions = restViewerMockMvc.perform(put("/api/v1/route/" + tour.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8).with(user(user))
                .content(testData));
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(404, result.getResponse().getStatus());
            }
        });
    }

    @Test
    public void getRoutes_UserHasTours_CorrectListOfTours() throws Exception {
        String expectedTourName1 = "Test1";
        String expectedTourName2 = "Test2";
        IBike expectedBike1 = this.bike;
        IBike expectedBike2 = facade.createBike(
                "TestBike99",
                new FrameNumber(5456),
                new Date(), new Date(),
                null,
                facade.getUserBy(new EMail(USER_EMAIL)).get()
        );
        Date expectedStartAt1 = new Date();
        Date expectedStartAt2 = new Date();
        Date expectedFinishedAt1 = new Date();
        Date expectedFinishedAt2 = new Date();
        List<GPS> expectedWayPoints1 = new ArrayList<>();
        List<GPS> expectedWayPoints2 = new ArrayList<>();
        expectedWayPoints1.add(new GPS(12, 13, "Test1"));
        expectedWayPoints1.add(new GPS(13, 14, "Test2"));
        expectedWayPoints1.add(new GPS(14, 15, "Test3"));
        expectedWayPoints2.add(new GPS(15, 16, "Test3"));
        expectedWayPoints2.add(new GPS(16, 17, "Test3"));
        expectedWayPoints2.add(new GPS(17, 18, "Test3"));
        double expectedLenghtInKm1 = 15;
        double expectedLenghtInKm2 = 16;

        ITour tour1 = facade.addTour(
                expectedTourName1,
                expectedBike1,
                expectedStartAt1,
                expectedFinishedAt1,
                expectedWayPoints1,
                expectedLenghtInKm1
        );

        ITour tour2 = facade.addTour(
                expectedTourName2,
                expectedBike2,
                expectedStartAt2,
                expectedFinishedAt2,
                expectedWayPoints2,
                expectedLenghtInKm2
        );

        ResultActions actions = restViewerMockMvc.perform(get("/api/v1/route").
                        contentType(TestUtil.APPLICATION_JSON_UTF8).
                        with(user(user))
        );
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(200, result.getResponse().getStatus());
                TourListEntryDTO tourList[] = TestUtil.jsonToObject(TourListEntryDTO[].class, result.getResponse().getContentAsString());
                assertEquals(2, tourList.length);
                assertEquals(expectedTourName1, tourList[0].name);
                assertEquals(expectedBike1.getId(), tourList[0].bikeID);
                assertEquals(FORMAT.format(expectedStartAt1), tourList[0].startAt);
                assertEquals(FORMAT.format(expectedFinishedAt1), tourList[0].finishedAt);
                assertEquals(expectedLenghtInKm1, tourList[0].lengthInKm);
                assertEquals(expectedTourName2, tourList[1].name);
                assertEquals(expectedBike2.getId(), tourList[1].bikeID);
                assertEquals(FORMAT.format(expectedStartAt2), tourList[1].startAt);
                assertEquals(FORMAT.format(expectedFinishedAt2), tourList[1].finishedAt);
                assertEquals(expectedLenghtInKm2, tourList[1].lengthInKm);
            }
        });

    }

    @Test
    public void getRoutes_UserHasNoTours_EmptyList() throws Exception {
        IUser user2 = facade.registerUser(
                "testUser2",
                "dsfdsf",
                new EMail("test@test432.de"),
                new Address(
                        "lkdsfsdfjösd",
                        "11",
                        "HH",
                        "SH",
                        "21521",
                        "DE"
                ),
                new Date(),
                "TestPw",
                HawaiAuthority.USER
        );
        UserDetails user2Details = authenticationService.loadUserByUsername("test@test432.de");
        String expectedTourName1 = "Test1";
        String expectedTourName2 = "Test2";
        IBike expectedBike1 = this.bike;
        IBike expectedBike2 = facade.createBike(
                "TestBike99",
                new FrameNumber(5456),
                new Date(), new Date(),
                null,
                facade.getUserBy(new EMail(USER_EMAIL)).get()
        );
        Date expectedStartAt1 = new Date();
        Date expectedStartAt2 = new Date();
        Date expectedFinishedAt1 = new Date();
        Date expectedFinishedAt2 = new Date();
        List<GPS> expectedWayPoints1 = new ArrayList<>();
        List<GPS> expectedWayPoints2 = new ArrayList<>();
        expectedWayPoints1.add(new GPS(12, 13, "Test1"));
        expectedWayPoints1.add(new GPS(13, 14, "Test2"));
        expectedWayPoints1.add(new GPS(14, 15, "Test3"));
        expectedWayPoints2.add(new GPS(15, 16, "Test3"));
        expectedWayPoints2.add(new GPS(16, 17, "Test3"));
        expectedWayPoints2.add(new GPS(17, 18, "Test3"));
        double expectedLenghtInKm1 = 15;
        double expectedLenghtInKm2 = 16;

        ITour tour1 = facade.addTour(
                expectedTourName1,
                expectedBike1,
                expectedStartAt1,
                expectedFinishedAt1,
                expectedWayPoints1,
                expectedLenghtInKm1
        );

        ITour tour2 = facade.addTour(
                expectedTourName2,
                expectedBike2,
                expectedStartAt2,
                expectedFinishedAt2,
                expectedWayPoints2,
                expectedLenghtInKm2
        );

        ResultActions actions = restViewerMockMvc.perform(get("/api/v1/route").
                        contentType(TestUtil.APPLICATION_JSON_UTF8).
                        with(user(user2Details))
        );
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(200, result.getResponse().getStatus());
                TourListEntryDTO tourList[] = TestUtil.jsonToObject(TourListEntryDTO[].class, result.getResponse().getContentAsString());
                assertEquals(0, tourList.length);
            }
        });

    }

    @Test
    public void getRoute_RouteExistsAndUserIsOwner_TourWillBeSent() throws Exception {
        String expectedTourName1 = "Test1";
        String expectedTourName2 = "Test2";
        IBike expectedBike1 = this.bike;
        IBike expectedBike2 = facade.createBike(
                "TestBike99",
                new FrameNumber(5456),
                new Date(), new Date(),
                null,
                facade.getUserBy(new EMail(USER_EMAIL)).get()
        );
        Date expectedStartAt1 = new Date();
        Date expectedStartAt2 = new Date();
        Date expectedFinishedAt1 = new Date();
        Date expectedFinishedAt2 = new Date();
        List<GPS> expectedWayPoints1 = new ArrayList<>();
        List<GPS> expectedWayPoints2 = new ArrayList<>();
        expectedWayPoints1.add(new GPS(12, 13, "Test1"));
        expectedWayPoints1.add(new GPS(13, 14, "Test2"));
        expectedWayPoints1.add(new GPS(14, 15, "Test3"));
        expectedWayPoints2.add(new GPS(15, 16, "Test3"));
        expectedWayPoints2.add(new GPS(16, 17, "Test3"));
        expectedWayPoints2.add(new GPS(17, 18, "Test3"));
        double expectedLenghtInKm1 = 15;
        double expectedLenghtInKm2 = 16;

        ITour tour1 = facade.addTour(
                expectedTourName1,
                expectedBike1,
                expectedStartAt1,
                expectedFinishedAt1,
                expectedWayPoints1,
                expectedLenghtInKm1
        );

        ITour tour2 = facade.addTour(
                expectedTourName2,
                expectedBike2,
                expectedStartAt2,
                expectedFinishedAt2,
                expectedWayPoints2,
                expectedLenghtInKm2
        );

        ResultActions actions = restViewerMockMvc.perform(get("/api/v1/route/" + tour1.getId()).
                        contentType(TestUtil.APPLICATION_JSON_UTF8).
                        with(user(user))
        );
        actions.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult result) throws Exception {
                assertEquals(200, result.getResponse().getStatus());
                TourDTO tourDto = TestUtil.jsonToObject(TourDTO.class, result.getResponse().getContentAsString());
                assertEquals(expectedTourName1, tourDto.name);
                assertEquals(expectedBike1.getId(), tourDto.bikeID);
                assertEquals(FORMAT.format(expectedStartAt1), tourDto.startAt);
                assertEquals(FORMAT.format(expectedFinishedAt1), tourDto.finishedAt);
                assertEquals(expectedLenghtInKm1, tourDto.lengthInKm);
            }
        });
    }

    @Test
    public void getRoute_RouteExistsAndUserIsNotOwner_ErrorNotAuthorized() throws Exception {
        IUser user2 = facade.registerUser(
                "testUser2",
                "dsfdsf",
                new EMail("test@test432.de"),
                new Address(
                        "lkdsfsdfjösd",
                        "11",
                        "HH",
                        "SH",
                        "21521",
                        "DE"
                ),
                new Date(),
                "TestPw",
                HawaiAuthority.USER
        );
        UserDetails user2Details = authenticationService.loadUserByUsername("test@test432.de");
        String expectedTourName1 = "Test1";
        String expectedTourName2 = "Test2";
        IBike expectedBike1 = this.bike;
        IBike expectedBike2 = facade.createBike(
                "TestBike99",
                new FrameNumber(5456),
                new Date(), new Date(),
                null,
                facade.getUserBy(new EMail(USER_EMAIL)).get()
        );
        Date expectedStartAt1 = new Date();
        Date expectedStartAt2 = new Date();
        Date expectedFinishedAt1 = new Date();
        Date expectedFinishedAt2 = new Date();
        List<GPS> expectedWayPoints1 = new ArrayList<>();
        List<GPS> expectedWayPoints2 = new ArrayList<>();
        expectedWayPoints1.add(new GPS(12, 13, "Test1"));
        expectedWayPoints1.add(new GPS(13, 14, "Test2"));
        expectedWayPoints1.add(new GPS(14, 15, "Test3"));
        expectedWayPoints2.add(new GPS(15, 16, "Test3"));
        expectedWayPoints2.add(new GPS(16, 17, "Test3"));
        expectedWayPoints2.add(new GPS(17, 18, "Test3"));
        double expectedLenghtInKm1 = 15;
        double expectedLenghtInKm2 = 16;

        ITour tour1 = facade.addTour(
                expectedTourName1,
                expectedBike1,
                expectedStartAt1,
                expectedFinishedAt1,
                expectedWayPoints1,
                expectedLenghtInKm1
        );

        ITour tour2 = facade.addTour(
                expectedTourName2,
                expectedBike2,
                expectedStartAt2,
                expectedFinishedAt2,
                expectedWayPoints2,
                expectedLenghtInKm2
        );

        ResultActions actions = restViewerMockMvc.perform(get("/api/v1/route/" + tour1.getId()).
                        contentType(TestUtil.APPLICATION_JSON_UTF8).
                        with(user(user2Details))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void getRoute_RouteDoesNotExits_ErrorNotFound() throws Exception {
        String expectedTourName1 = "Test1";
        String expectedTourName2 = "Test2";
        IBike expectedBike1 = this.bike;
        IBike expectedBike2 = facade.createBike(
                "TestBike99",
                new FrameNumber(5456),
                new Date(), new Date(),
                null,
                facade.getUserBy(new EMail(USER_EMAIL)).get()
        );
        Date expectedStartAt1 = new Date();
        Date expectedStartAt2 = new Date();
        Date expectedFinishedAt1 = new Date();
        Date expectedFinishedAt2 = new Date();
        List<GPS> expectedWayPoints1 = new ArrayList<>();
        List<GPS> expectedWayPoints2 = new ArrayList<>();
        expectedWayPoints1.add(new GPS(12, 13, "Test1"));
        expectedWayPoints1.add(new GPS(13, 14, "Test2"));
        expectedWayPoints1.add(new GPS(14, 15, "Test3"));
        expectedWayPoints2.add(new GPS(15, 16, "Test3"));
        expectedWayPoints2.add(new GPS(16, 17, "Test3"));
        expectedWayPoints2.add(new GPS(17, 18, "Test3"));
        double expectedLenghtInKm1 = 15;
        double expectedLenghtInKm2 = 16;

        ITour tour1 = facade.addTour(
                expectedTourName1,
                expectedBike1,
                expectedStartAt1,
                expectedFinishedAt1,
                expectedWayPoints1,
                expectedLenghtInKm1
        );

        ITour tour2 = facade.addTour(
                expectedTourName2,
                expectedBike2,
                expectedStartAt2,
                expectedFinishedAt2,
                expectedWayPoints2,
                expectedLenghtInKm2
        );

        ResultActions actions = restViewerMockMvc.perform(get("/api/v1/route/" + 999999).
                        contentType(TestUtil.APPLICATION_JSON_UTF8).
                        with(user(user))
        ).andExpect(status().isNotFound());
    }

    @Test
    public void deleteRoute_CorrectId_TourIsDeleted() throws Exception {
        List<GPS> waypoints = new ArrayList<>();
        ITour tour = facade.addTour("Test", bike, new Date(), new Date(), waypoints, 15);
        ResultActions actions = restViewerMockMvc.perform(
                delete("/api/v1/route/" + tour.getId()).
                        contentType(TestUtil.APPLICATION_JSON_UTF8).
                        with(user(user))
        ).andExpect(status().isOk());

    }

    @Test
    public void deleteRoute_IncorrectId_ResonseWithError404() throws Exception {
        ResultActions actions = restViewerMockMvc.perform(
                delete("/api/v1/route/" + 458).
                        contentType(TestUtil.APPLICATION_JSON_UTF8).
                        with(user(user))
        ).andExpect(status().isNotFound());
    }
}

