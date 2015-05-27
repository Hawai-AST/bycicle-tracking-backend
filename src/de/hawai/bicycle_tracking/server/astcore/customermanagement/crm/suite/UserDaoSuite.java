package de.hawai.bicycle_tracking.server.astcore.customermanagement.crm.suite;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUserDao;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.User;
import de.hawai.bicycle_tracking.server.crm.suite.SuiteCrmConnector;
import de.hawai.bicycle_tracking.server.crm.suite.token.request.GetEntryListToken;
import de.hawai.bicycle_tracking.server.crm.suite.token.request.SetEntryToken;
import de.hawai.bicycle_tracking.server.crm.suite.token.response.SetEntryResponseToken;
import de.hawai.bicycle_tracking.server.utility.value.EMail;

@Repository("suiteRepository")
public class UserDaoSuite implements IUserDao {

	private static final String MODULE = "Accounts";
	private SuiteCrmConnector connector;
	
	private static final List<String> SELECT_FIELDS = Arrays.asList(
			UserSerializationHelperSuite.NAME, UserSerializationHelperSuite.FIRSTNAME, UserSerializationHelperSuite.EMAIL,
			UserSerializationHelperSuite.PASSWORD, UserSerializationHelperSuite.BIRTHDAY, UserSerializationHelperSuite.ADDRESS_STREET,
			UserSerializationHelperSuite.ADDRESS_CITY, UserSerializationHelperSuite.ADDRESS_STATE,
			UserSerializationHelperSuite.ADDRESS_POSTCODE, UserSerializationHelperSuite.ADDRESS_COUNTRY,
			UserSerializationHelperSuite.AUTHORITY, UserSerializationHelperSuite.UUID);

	private UserDaoSuite() {
		super();
	}

	@Autowired
	public UserDaoSuite(SuiteCrmConnector connector) {
		super();
		this.connector = connector;
	}

	@Override
	public List<User> findAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<User> findAll(Sort sort) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends User> List<S> save(Iterable<S> entities) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void flush() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends User> S saveAndFlush(S entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteInBatch(Iterable<User> entities) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAllInBatch() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Page<User> findAll(Pageable pageable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends User> S save(S entity) {
		
//		todo(fap): inject object mapper
		ObjectMapper mapper = new ObjectMapper();
		
		addCustomUserSerializer(mapper);
		
		try {
			handleFaultyInput(entity);
		} catch (RegistrationException e1) {
			e1.printStackTrace();
			return null;
		}
		try {
			SetEntryResponseToken postSetEntry = (SetEntryResponseToken) connector.postSetEntry(
					new SetEntryToken(connector.getSessionId(), MODULE,
							mapper.writeValueAsString(entity)), SetEntryResponseToken.class);
			entity.setId(UUID.fromString(postSetEntry.getId()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return entity;
	}

	private void addCustomUserSerializer(ObjectMapper mapper) {
		SimpleModule module = new SimpleModule();
		module.addSerializer(User.class, new UserSerializerSuite());
		mapper.registerModule(module);
	}

	private <S extends User> S handleFaultyInput(S entity) throws RegistrationException {
		UUID uuidFromFrontend = entity.getId();
		// UUID empty => fill from CRM if email exists
		Optional<User> userFromSuite = getByMailAddress(entity.getMailAddress());
		if (null == uuidFromFrontend && userFromSuite.isPresent()) {
				entity.setId(userFromSuite.get().getId());
				return entity;
		}
		User userFromCRM = getOne(entity.getId());
		// Non existent UUID
		if (userFromCRM == null) {
			throw new RegistrationException("No existing Account with this ID.");
		}
		// Existent UUI but EMail not matching
		if (!uuidFromFrontend.equals(userFromCRM.getId())) {
			throw new RegistrationException("ID for given EMail doesn't match ID in SuiteCRM.");
		}
		return entity;
	}

	@Override
	public long count() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(User entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(Iterable<? extends User> entities) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<User> getByName(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Optional<User> getByMailAddress(EMail emailAddress) {
		int max_results = 1;
		int returnDeleted = 0;
		String query = "accounts.id in ( " + "SELECT eabr.bean_id "
				+ "FROM email_addr_bean_rel eabr JOIN email_addresses ea "
				+ "WHERE ea.id = eabr.email_address_id and eabr.deleted=0 AND ea.email_address = '"
				+ StringEscapeUtils.escapeSql(emailAddress.getMailAddress()) + "')";
		return Optional.ofNullable((User) connector.postGetEntryList(
				new GetEntryListToken(
						connector.getSessionId(), MODULE, query, max_results, returnDeleted, SELECT_FIELDS), User.class));
	}

	@Override
	public List<User> findAll(Iterable<UUID> ids) {
		throw new UnsupportedOperationException();
	}

	@Override
	public User getOne(UUID id) {
		int max_results = 1;
		int returnDeleted = 0;
		String query = "accounts.id = '" + id.toString() + "'";
		return (User) connector.postGetEntryList(
				new GetEntryListToken(
						connector.getSessionId(), MODULE, query, max_results, returnDeleted, SELECT_FIELDS), User.class);
	}

	@Override
	public User findOne(UUID id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean exists(UUID id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(UUID id) {
		throw new UnsupportedOperationException();
	}
}
