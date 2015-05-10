package de.hawai.bicycle_tracking.server.astcore.tourmanagement;

import de.hawai.bicycle_tracking.server.AppConfig;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBikeManagement;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.ICustomerManagement;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.security.HawaiAuthority;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;
import de.hawai.bicycle_tracking.server.utility.value.GPS;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AppConfig.class)
@Transactional(noRollbackFor = Exception.class)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class TourManagementTest extends TestCase {

    @Autowired
    private ITourManagement tourManagement;

    @Autowired
    private ICustomerManagement customerManagement;

    @Autowired
    private IBikeManagement bikeManagement;

    private IUser user1;
    private IUser user2;
    private IBike bike1;
    private IBike bike2;
    private List<GPS> waypoints;
    
    @Before
    public void setup(){
         user1 = customerManagement.registerUser(
                 "Mustermann",
                 "Max",
                 new EMail("max.mustermann@test.com"),
                 new Address("MusterStraße", "11", "Musterhausen", "MusterState", "01923", "Deutschland"),
                 new Date(),
                 "123456",
                 HawaiAuthority.USER
        );

        user2 = customerManagement.registerUser(
                "Mustermann2",
                "Max2",
                new EMail("max.mustermann2@test.com"),
                new Address("MusterStraße", "12", "Musterhausen", "MusterState", "01923", "Deutschland"),
                new Date(),
                "1234567",
                HawaiAuthority.USER
        );

        bike2 = bikeManagement.createBike(
                "TestType",
                new FrameNumber(1234),
                new Date(),
                new Date(),
                null,
                user2
        );

        bike1 = bikeManagement.createBike(
                "TestType2",
                new FrameNumber(4321),
                new Date(),
                new Date(),
                null,
                user1
        );

        waypoints = new ArrayList<>();
        waypoints.add(new GPS(11.4, 11.5, "pos1"));
        waypoints.add(new GPS(11.6, 11.7, "pos2"));
        waypoints.add(new GPS(11.8, 11.9, "pos3"));
        waypoints.add(new GPS(12.0, 12.1, "pos4"));
        waypoints.add(new GPS(12.2, 12.3, "pos5"));
        waypoints.add(new GPS(12.4, 12.5, "pos6"));
        waypoints.add(new GPS(12.6, 12.7, "pos7"));
    }

    @Test
    public void addTour_TourDoesNotExists_TourIsSaved() throws Exception {
        ITour tour = tourManagement.addTour(
                "TestTour",
                bike1,
                new Date(),
                new Date(),
                waypoints,
                15.0
        );
        ITour tourGet = tourManagement.getTourById(tour.getId());
        assertEquals(tour, tourGet);
        assertEquals(tour.getBike(), bike1);
        assertEquals(tour.getName(), "TestTour");
    }

    @Test
    public void getToursByUser_UserAssignedToursAreInDatabse_ListsOfUserAssignedToursHaveCorrectValues() throws Exception {
        ITour tour1 = tourManagement.addTour(
                "TestTour",
                bike1,
                new Date(),
                new Date(),
                waypoints,
                15.0
        );

        ITour tour2 = tourManagement.addTour(
                "TestTour",
                bike2,
                new Date(),
                new Date(),
                waypoints,
                15.0
        );

        List<ITour> tours1 = tourManagement.getToursByUser(user1);
        assertTrue(tours1.size() == 1);
        assertEquals(tours1.get(0), tour1);
    }

    @Test
    public void updateTour_OneTourInDatabaseExists_TourIsCorrectlyChanged() throws Exception {
        ITour tour1 = tourManagement.addTour(
                "TestTour",
                bike1,
                new Date(),
                new Date(),
                waypoints,
                15.0
        );

        long tourId = tour1.getId();
        String changedName = "Changed";
        Date changedRodeAt = new Date(System.currentTimeMillis() - 10000);
        Date changedFinishedAt = new Date(System.currentTimeMillis() - 9000);
        List<GPS> changedWaypoints = new ArrayList<>(waypoints);
        changedWaypoints.remove(0);
        double changedLength = 12;
        tourManagement.updateTour(
                tour1,
                changedName,
                bike2,
                changedRodeAt,
                changedFinishedAt,
                changedWaypoints,
                changedLength
        );
        ITour tour2 = tourManagement.getTourById(tourId);
        assertEquals(tour2.getName(), changedName);
        assertEquals(tour2.getBike(), bike2);
        assertEquals(tour2.getRodeAt(), changedRodeAt);
        assertEquals(tour2.getFinishedAt(), changedFinishedAt);
        assertEquals(tour2.getWaypoints().size(), changedWaypoints.size());
        assertEquals(tour2.getWaypoints().get(0), changedWaypoints.get(0));
        assertEquals(tour2.getWaypoints().get(1), changedWaypoints.get(1));
        assertEquals(tour2.getWaypoints().get(2), changedWaypoints.get(2));
        assertEquals(tour2.getWaypoints().get(3), changedWaypoints.get(3));
        assertEquals(tour2.getWaypoints().get(4), changedWaypoints.get(4));
        assertEquals(tour2.getWaypoints().get(5), changedWaypoints.get(5));
        assertEquals(tour2.getWaypoints(), changedWaypoints);
        assertEquals(tour2.getLengthInKm(), changedLength);
    }

    @Test
    public void deleteTour_TourExists_TourIsDeleted(){
        ITour tour1 = tourManagement.addTour(
                "TestTour",
                bike1,
                new Date(),
                new Date(),
                waypoints,
                15.0
        );

        tourManagement.deleteTour(tour1);
        assertNull(tourManagement.getTourById(tour1.getId()));
    }
}

