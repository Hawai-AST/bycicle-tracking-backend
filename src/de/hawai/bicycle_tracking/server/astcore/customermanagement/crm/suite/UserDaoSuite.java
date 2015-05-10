package de.hawai.bicycle_tracking.server.astcore.customermanagement.crm.suite;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUserDao;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.User;
import de.hawai.bicycle_tracking.server.crm.suite.SuiteCrmConnector;
import de.hawai.bicycle_tracking.server.crm.suite.token.GetEntryListToken;
import de.hawai.bicycle_tracking.server.crm.suite.token.SetEntryResponseToken;
import de.hawai.bicycle_tracking.server.crm.suite.token.SetEntryToken;
import de.hawai.bicycle_tracking.server.utility.value.EMail;

@Repository
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

	public UserDaoSuite(SuiteCrmConnector connector) {
		super();
		this.connector = connector;
	}

	@Override
	public List<User> findAll() {
		throw new NotImplementedException();
	}

	@Override
	public List<User> findAll(Sort sort) {
		throw new NotImplementedException();
	}

	@Override
	public <S extends User> List<S> save(Iterable<S> entities) {
		throw new NotImplementedException();
	}

	@Override
	public void flush() {
		throw new NotImplementedException();
	}

	@Override
	public <S extends User> S saveAndFlush(S entity) {
		throw new NotImplementedException();
	}

	@Override
	public void deleteInBatch(Iterable<User> entities) {
		throw new NotImplementedException();
	}

	@Override
	public void deleteAllInBatch() {
		throw new NotImplementedException();
	}

	@Override
	public Page<User> findAll(Pageable pageable) {
		throw new NotImplementedException();
	}

	@Override
	public <S extends User> S save(S entity) {
		
//		todo(fap): inject object mapper
		ObjectMapper mapper = new ObjectMapper();
		
		SimpleModule module = new SimpleModule();
		module.addSerializer(User.class, new UserSerializerSuite());
		mapper.registerModule(module);
		
		mapper.registerModule(module);
		
		retrieveUuidFromSuite(entity);
		try {
			SetEntryResponseToken postSetEntry = (SetEntryResponseToken) connector.postSetEntry(
					new SetEntryToken(connector.getSessionId(), MODULE,
							mapper.writeValueAsString(entity)), SetEntryResponseToken.class);
			entity.setId(UUID.fromString(postSetEntry.getId()));
		} catch (JsonProcessingException e) {
			// TODO(fap) Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO(fap) Auto-generated catch block
			e.printStackTrace();
		}
		return entity;
	}

	private <S extends User> void retrieveUuidFromSuite(S entity) {
		UUID uuid = entity.getId();
		if (null == uuid || UUID.fromString("").equals(uuid)) {
			Optional<User> userWithId = getByMailAddress(entity.getMailAddress());
			if (userWithId.isPresent()) {
				entity.setId(userWithId.get().getId());
			}
		}
	}

	@Override
	public long count() {
		throw new NotImplementedException();
	}

	@Override
	public void delete(User entity) {
		throw new NotImplementedException();
	}

	@Override
	public void delete(Iterable<? extends User> entities) {
		throw new NotImplementedException();
	}

	@Override
	public void deleteAll() {
		throw new NotImplementedException();
	}

	@Override
	public Optional<User> getByName(String name) {
		throw new NotImplementedException();
	}

	@Override
	public Optional<User> getByMailAddress(EMail emailAddress) {
		;

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
		throw new NotImplementedException();
	}

	@Override
	public User getOne(UUID id) {
		throw new NotImplementedException();
	}

	@Override
	public User findOne(UUID id) {
		throw new NotImplementedException();
	}

	@Override
	public boolean exists(UUID id) {
		throw new NotImplementedException();
	}

	@Override
	public void delete(UUID id) {
		throw new NotImplementedException();
	}
}
