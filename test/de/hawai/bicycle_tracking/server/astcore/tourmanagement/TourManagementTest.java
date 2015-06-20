package de.hawai.bicycle_tracking.server.astcore.tourmanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.hawai.bicycle_tracking.server.utility.value.*;
import junit.framework.TestCase;

import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import de.hawai.bicycle_tracking.server.AppConfig;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.BikeType;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBikeManagement;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBikeTypeDao;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.ICustomerManagement;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.security.HawaiAuthority;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AppConfig.class)
@Transactional(noRollbackFor = Exception.class)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class TourManagementTest extends TestCase {

    private static final String BIKE_NAME = "mae byke";

	@Autowired
    private ITourManagement tourManagement;

    @Autowired
    private ICustomerManagement customerManagement;

    @Autowired
    private IBikeManagement bikeManagement;
    
    @Autowired
	@Qualifier("bikeTypeDAO")
    private IBikeTypeDao bikeTypeRepository;

    private IUser user1;
    private IUser user2;
    private IBike bike1;
    private IBike bike2;
    private List<GPS> waypoints;
	private BikeType bikeType1;
	private BikeType bikeType2;
    
    @Before
    public void setup(){
    	
    	bikeType1 = bikeTypeRepository.save(new BikeType("City Bike", "for the city", Period.weeks(4)));
    	bikeType2 = bikeTypeRepository.save(new BikeType("Cross Bike", "for the cross", Period.weeks(2)));
    	
         user1 = customerManagement.registerUser(
                 "Mustermann",
                 "Max",
                 new EMail("max.mustermann@test.com"),
                 new Address("MusterStraße", "11", "Musterhausen", "MusterState", "01923", "Deutschland"),
                 new Date(),
                 "123456",
                 Gender.NONE, HawaiAuthority.USER
        );

        user2 = customerManagement.registerUser(
                "Mustermann2",
                "Max2",
                new EMail("max.mustermann2@test.com"),
                new Address("MusterStraße", "12", "Musterhausen", "MusterState", "01923", "Deutschland"),
                new Date(),
                "1234567",
                Gender.NONE, HawaiAuthority.USER
        );

        bike2 = bikeManagement.createBike(
                bikeType1,
                new FrameNumber(1234),
                new Date(),
                new Date(),
                null,
                user2,
                BIKE_NAME
        );

        bike1 = bikeManagement.createBike(
                bikeType2,
                new FrameNumber(4321),
                new Date(),
                new Date(),
                null,
                user1,
                BIKE_NAME
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
        ITour tourGet = tourManagement.getTourById(tour.getId()).get();
        assertEquals(tour, tourGet);
        assertEquals(tour.getBike(), bike1.getId());
        assertEquals(tour.getName(), "TestTour");
    }

    @Test(expected = AddTourFailedException.class)
    public void addTour_ParameterNameMissing_ThrowsError() throws AddTourFailedException {
        tourManagement.addTour(
                null,
                bike1,
                new Date(),
                new Date(),
                waypoints,
                15.0
        );
    }

    @Test(expected = AddTourFailedException.class)
    public void addTour_ParameterBikeMissing_ThrowsError() throws AddTourFailedException {
        tourManagement.addTour(
                "test",
                null,
                new Date(),
                new Date(),
                waypoints,
                15.0
        );
    }

    @Test(expected = AddTourFailedException.class)
    public void addTour_ParameterStartAtMissing_ThrowsError() throws AddTourFailedException {
        tourManagement.addTour(
                "test",
                bike1,
                null,
                new Date(),
                waypoints,
                15.0
        );
    }

    @Test(expected = AddTourFailedException.class)
    public void addTour_ParameterFinishedAtMissing_ThrowsError() throws AddTourFailedException {
        tourManagement.addTour(
                "test",
                bike1,
                new Date(),
                null,
                waypoints,
                15.0
        );
    }

    @Test(expected = AddTourFailedException.class)
    public void addTour_ParameterWaypointsMissing_ThrowsError() throws AddTourFailedException {
        tourManagement.addTour(
                "test",
                bike1,
                new Date(),
                new Date(),
                null,
                15.0
        );
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
    public void getToursByUser_UserHasNoToursInDataBase_ListsOfUserAssignedToursIsEmpty() throws Exception {
        ITour tour1 = tourManagement.addTour(
                "TestTour",
                bike2,
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
        assertTrue(tours1.isEmpty());
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

        UUID tourId = tour1.getId();
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

    }

    @Test(expected = UpdateTourFailedException.class)
    public void updateTour_OneTourInDatabaseExistsChangesWithMissingDataTour_ThrowsException()
            throws AddTourFailedException, UpdateTourFailedException {
        ITour tour1 = tourManagement.addTour(
                "TestTour",
                bike1,
                new Date(),
                new Date(),
                waypoints,
                15.0
        );

        UUID tourId = tour1.getId();
        String changedName = "Changed";
        Date changedRodeAt = new Date(System.currentTimeMillis() - 10000);
        Date changedFinishedAt = new Date(System.currentTimeMillis() - 9000);
        List<GPS> changedWaypoints = new ArrayList<>(waypoints);
        changedWaypoints.remove(0);
        double changedLength = 12;
        tourManagement.updateTour(
                null,
                changedName,
                bike2,
                changedRodeAt,
                changedFinishedAt,
                changedWaypoints,
                changedLength
        );
    }

    @Test(expected = UpdateTourFailedException.class)
    public void updateTour_OneTourInDatabaseExistsChangesWithMissingDataName_ThrowsException()
            throws AddTourFailedException, UpdateTourFailedException {
        ITour tour1 = tourManagement.addTour(
                "TestTour",
                bike1,
                new Date(),
                new Date(),
                waypoints,
                15.0
        );


        UUID tourId = tour1.getId();
        String changedName = "Changed";
        Date changedRodeAt = new Date(System.currentTimeMillis() - 10000);
        Date changedFinishedAt = new Date(System.currentTimeMillis() - 9000);
        List<GPS> changedWaypoints = new ArrayList<>(waypoints);
        changedWaypoints.remove(0);
        double changedLength = 12;
        tourManagement.updateTour(
                tour1,
                null,
                bike2,
                changedRodeAt,
                changedFinishedAt,
                changedWaypoints,
                changedLength
        );
    }

    @Test(expected = UpdateTourFailedException.class)
    public void updateTour_OneTourInDatabaseExistsChangesWithMissingDataBike_ThrowsException()
            throws AddTourFailedException, UpdateTourFailedException {
        ITour tour1 = tourManagement.addTour(
                "TestTour",
                bike1,
                new Date(),
                new Date(),
                waypoints,
                15.0
        );

        UUID tourId = tour1.getId();
        String changedName = "Changed";
        Date changedRodeAt = new Date(System.currentTimeMillis() - 10000);
        Date changedFinishedAt = new Date(System.currentTimeMillis() - 9000);
        List<GPS> changedWaypoints = new ArrayList<>(waypoints);
        changedWaypoints.remove(0);
        double changedLength = 12;
        tourManagement.updateTour(
                tour1,
                changedName,
                null,
                changedRodeAt,
                changedFinishedAt,
                changedWaypoints,
                changedLength
        );
    }

    @Test(expected = UpdateTourFailedException.class)
    public void updateTour_OneTourInDatabaseExistsChangesWithMissingDataStartAt_ThrowsException()
            throws Exception {
        ITour tour1 = tourManagement.addTour(
                "TestTour",
                bike1,
                new Date(),
                new Date(),
                waypoints,
                15.0
        );

        UUID tourId = tour1.getId();
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
                null,
                changedFinishedAt,
                changedWaypoints,
                changedLength
        );
    }

    @Test(expected = UpdateTourFailedException.class)
    public void updateTour_OneTourInDatabaseExistsChangesWithMissingDataFinishedAt_ThrowsException()
            throws Exception {
        ITour tour1 = tourManagement.addTour(
                "TestTour",
                bike1,
                new Date(),
                new Date(),
                waypoints,
                15.0
        );

        UUID tourId = tour1.getId();
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
                null,
                changedWaypoints,
                changedLength
        );
    }

    @Test(expected = UpdateTourFailedException.class)
    public void updateTour_OneTourInDatabaseExistsChangesWithMissingDataWaypoints_ThrowsException()
            throws Exception {
        ITour tour1 = tourManagement.addTour(
                "TestTour",
                bike1,
                new Date(),
                new Date(),
                waypoints,
                15.0
        );

        UUID tourId = tour1.getId();
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
                null,
                changedLength
        );
    }

    @Test
    public void deleteTour_TourExists_TourIsDeleted() throws AddTourFailedException {
        ITour tour1 = tourManagement.addTour(
                "TestTour",
                bike1,
                new Date(),
                new Date(),
                waypoints,
                15.0
        );

        tourManagement.deleteTour(tour1);
        assertNull(tourManagement.getTourById(tour1.getId()).orElse(null));
    }

    @Test
    public void deleteTour_TourDoesntExists_TourIsDeleted() throws AddTourFailedException {
        ITour tour1 = tourManagement.addTour(
                "TestTour",
                bike1,
                new Date(),
                new Date(),
                waypoints,
                15.0
        );

        tourManagement.deleteTour(tour1);
        tourManagement.deleteTour(tour1);
        //assertNotNull(tourManagement.getTourById(tour1.getId()).get());
    }
}

