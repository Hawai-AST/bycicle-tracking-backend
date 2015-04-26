package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import org.junit.Assert;
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

import de.hawai.bicycle_tracking.server.AppConfig;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.facade.Facade;
import de.hawai.bicycle_tracking.server.security.HawaiAuthority;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AppConfig.class)
@Transactional(noRollbackFor = Exception.class)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class BikeIntegrationTest {
	@Autowired
	private BikeDao bikeRepository;

	@Autowired
	private SellingLocationDao locationRepository;

	@Autowired
	private Facade facade;

	private SellingLocation sellingLocation;
	private IUser user;

	@Before
	public void setup() {
		if (this.sellingLocation == null) {
			sellingLocation = new SellingLocation(new Address("a", "a", "a", "a", "a", "a"), "a");
			this.locationRepository.save(sellingLocation);
		}

		if (this.user == null) {
			user = facade.registerUser("", "", new EMail("a@a.com"), new Address("", "", "", "", "", ""), new Date(), "",
					HawaiAuthority.USER);
		}
	}

	@Test
	public void findBySoldLocation_LocationWithExistingBike_BikeIsFound() {
		Bike bike = new Bike("Stadt", new FrameNumber(101), new Date(1), new Date(), sellingLocation, user);
		this.bikeRepository.save(bike);

		Assert.assertEquals(this.bikeRepository.findBySoldLocation(sellingLocation).size(), 1);
	}

	@Test
	public void findByOwner_OwnerWithExistingBike_BikeIsFound() {
		Bike bike = new Bike("Cross", new FrameNumber(101), new Date(1), new Date(), sellingLocation, user);
		this.bikeRepository.save(bike);

		Assert.assertEquals(this.bikeRepository.findByOwner(user).size(), 1);
	}
}
