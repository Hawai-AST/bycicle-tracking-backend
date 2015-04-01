package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
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
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AppConfig.class)
@Transactional(noRollbackFor = Exception.class)
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@IntegrationTest
public class UserIT {

	private static final String NAME = "Name";
	private static final String FIRST_NAME = "FirstName";
	private static final EMail E_MAIL_ADDRESS = new EMail("foo@bar.com");
	private static final Address ADDRESS = new Address("Foostreet 1a", "Barheim", "DC", "1337");
	private static final Date BIRTHDATE = new Date(0);

	@Autowired
	private UserDao userDao;

	private User user;

	@Before
	public void setup() {
		user = new User(NAME, FIRST_NAME, E_MAIL_ADDRESS, ADDRESS, BIRTHDATE);
		user = userDao.save(user);
	}


	@Test
	public void createdUserCanBeFoundByName() throws Exception {
		assertThat(user).isEqualTo(userDao.getByName(NAME));
	}

	@Test
	public void createdUserCanBeFoundByID() throws Exception {
		User userFromDB = userDao.getOne(user.getId());
		assertThat(user).isEqualTo(userFromDB);
	}

	@Test
	public void userGetsSerializedCorrectly() throws Exception {
		User userFromDB = userDao.getOne(user.getId());
		assertThat(user).isEqualTo(userFromDB);
		assertThat(user.getName()).isEqualTo(userFromDB.getName());
		assertThat(user.getFirstName()).isEqualTo(userFromDB.getFirstName());
		assertThat(user.geteMailAddress()).isEqualTo(userFromDB.geteMailAddress());
		assertThat(user.getAddress()).isEqualTo(userFromDB.getAddress());
		assertThat(user.getBirthdate()).isEqualTo(userFromDB.getBirthdate());
		assertThat(NAME).isEqualTo(userFromDB.getName());
		assertThat(FIRST_NAME).isEqualTo(userFromDB.getFirstName());
		assertThat(E_MAIL_ADDRESS).isEqualTo(userFromDB.geteMailAddress());
		assertThat(ADDRESS).isEqualTo(userFromDB.getAddress());
		assertThat(BIRTHDATE).isEqualTo(userFromDB.getBirthdate());
	}

	@Test
	public void nonExistentUsersCantBeFound() throws Exception {
		assertThat(userDao.getByName("Foobar")).isNull();
	}

}
