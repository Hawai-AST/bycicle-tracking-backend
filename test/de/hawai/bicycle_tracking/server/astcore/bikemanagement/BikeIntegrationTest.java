package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import java.util.Date;

import de.hawai.bicycle_tracking.server.AppConfig;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.User;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.UserDao;
import de.hawai.bicycle_tracking.server.security.HawaiAuthority;
import de.hawai.bicycle_tracking.server.utility.value.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.*;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AppConfig.class)
@Transactional(noRollbackFor = Exception.class)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class BikeIntegrationTest {
    @Autowired
    private BikeDao bikeRepository;

    @Autowired
    private SellingLocationDao locationRepository;

    @Autowired
    private UserDao userRepository;

    private SellingLocation sellingLocation;
    private User user;

    @Before
    public void setup() {
        if (this.sellingLocation == null) {
            sellingLocation = new SellingLocation(new Address("a", "a", "a", "a", "a", "a"), "a");
            this.locationRepository.save(sellingLocation);
        }

        if (this.user == null) {
            user = new User("", "", new EMail("a@a.com"), new Address("", "", "", "", "", ""), new Date(), "", HawaiAuthority.USER);
            this.userRepository.save(user);
        }
    }

    @Test
    public void testFindBySoldLocation() {
        Bike bike = new Bike("Stadt", new FrameNumber(101), new Date(1), new Date(), sellingLocation, user);
        this.bikeRepository.save(bike);

        Assert.assertEquals(this.bikeRepository.findBySoldLocation(sellingLocation).size(), 1);
    }

    @Test
    public void testFindByOwner() {
        Bike bike = new Bike("Cross", new FrameNumber(101), new Date(1), new Date(), sellingLocation, user);
        this.bikeRepository.save(bike);

        Assert.assertEquals(this.bikeRepository.findByOwner(user).size(), 1);
    }
}
