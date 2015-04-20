package de.hawai.bicycle_tracking.server.facade;

import de.hawai.bicycle_tracking.server.AppConfig;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.ISellingLocation;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.SellingLocation;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.SellingLocationDao;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;
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

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AppConfig.class)
@Transactional(noRollbackFor = Exception.class)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class FacadeIT {

	private static final String NAME = "Name";
	private static final String FIRST_NAME = "FirstName";
	private static final EMail E_MAIL_ADDRESS = new EMail("foo@bar.com");
	private static final Address ADDRESS = new Address("Foostreet", "1a", "Barheim", "DC", "1337", "Germany");
	private static final Date BIRTHDATE = new Date(0);
	private static final String PASSWORD = "TestingPassword";

	private static final String BIKE_TYPE = "Bike";
	private static final FrameNumber BIKE_FRAME_NUMBER = new FrameNumber(101);
	private static final Date BIKE_BUY_DATE = new Date(1);
	private static final Date BIKE_NEXT_MAINTENANCE = new Date(2);

	private static final String SELLING_LOCATION_NAME = "NoName";
	private static final Address SELLING_LOCATION_ADDRESS = new Address("Somewhere", "-1", "Bielefeld", "NIE", "21337", "Germany");

	@Autowired
	private Facade facade;

	@Autowired
	private SellingLocationDao sellingLocationDao;

	private IUser user;

	private IBike bike;

	private ISellingLocation sellingLocation;

	@Before
	public void setup() {
		user = facade.registerUser(NAME, FIRST_NAME, E_MAIL_ADDRESS, ADDRESS, BIRTHDATE, PASSWORD);

		sellingLocation = new SellingLocation(SELLING_LOCATION_ADDRESS, SELLING_LOCATION_NAME);
		sellingLocationDao.save((SellingLocation)sellingLocation);

		bike = facade.createBike(BIKE_TYPE, BIKE_FRAME_NUMBER, BIKE_BUY_DATE, BIKE_NEXT_MAINTENANCE, sellingLocation, user);
	}

	@Test
	public void createdUserCanBeRetrieved() {
		assertThat(facade.getUserBy(E_MAIL_ADDRESS).isPresent()).isTrue();
	}

	@Test
	public void findByOwner_ValidOwner_IsCreatedBike() {
		List<? extends IBike> bikes = facade.findByOwner(user);
		assertThat(bikes).hasSize(1);
		assertThat(bikes.get(0)).isEqualTo(bike);
	}
}
