package de.hawai.bicycle_tracking.server.astcore.bikemanagement.crm.suite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.Bike;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBikeDao;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.ISellingLocation;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.crm.suite.SuiteCrmConnector;
import de.hawai.bicycle_tracking.server.crm.suite.token.request.GetEntryListToken;
import de.hawai.bicycle_tracking.server.crm.suite.token.request.GetRelationshipsToken;
import de.hawai.bicycle_tracking.server.crm.suite.token.request.SetEntryToken;
import de.hawai.bicycle_tracking.server.crm.suite.token.response.GetEntryListResponseToken;
import de.hawai.bicycle_tracking.server.crm.suite.token.response.SetEntryResponseToken;

@Repository("suiteBikeDao")
public class BikeDaoSuite implements IBikeDao {
	
	private static final String MODULE = "HAWAI_Bikes";
	private static final List<String> SELECT_FIELDS = BikeSerializationHelperSuite.getList();
	private SuiteCrmConnector connector;

	private BikeDaoSuite() {
		super();
	}
	
	@Autowired
	public BikeDaoSuite(SuiteCrmConnector connector) {
		super();
		this.connector = connector;
	}

	@Override
	public List<Bike> findAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Bike> findAll(Sort sort) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Bike> findAll(Iterable<UUID> ids) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends Bike> List<S> save(Iterable<S> entities) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void flush() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends Bike> S saveAndFlush(S entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteInBatch(Iterable<Bike> entities) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAllInBatch() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Bike getOne(UUID id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Page<Bike> findAll(Pageable pageable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends Bike> S save(S entity) {
		// UUID supplied from frontend
		if (null != entity.getId()) {
			Bike entityOnSuite = findOne(entity.getId());
			if (null == entityOnSuite) {
				try {
					throw new Exception("Invalid Bike ID: " + entity.getId());
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		
		ObjectMapper mapper = new ObjectMapper();
		addCustomBikeSerializer(mapper);
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
	
	private void addCustomBikeSerializer(ObjectMapper mapper) {
		SimpleModule module = new SimpleModule();
		module.addSerializer(Bike.class, new BikeSerializerSuite());
		mapper.registerModule(module);
	}

	@Override
	public Bike findOne(UUID id) {
		int max_results = 1;
		int returnDeleted = 0;
		String query = "hawai_bikes.id = '" + id.toString() + "'";
		List<HashMap<String, Object>> getOwnerLinkArray = buildGetOwnerLinkArray();
		GetEntryListToken getEntryList = new GetEntryListToken(
				connector.getSessionId(), MODULE, query, max_results, returnDeleted, SELECT_FIELDS);
		getEntryList.setLinkNameArray(getOwnerLinkArray);
		GetEntryListResponseToken responseToken = (GetEntryListResponseToken) connector.postGetEntryList(
				getEntryList,
						GetEntryListResponseToken.class);
		return new BikeResponseTokenDeserializerSuite().deserialize(responseToken)
						.get(0);
	}

	private List<HashMap<String, Object>> buildGetOwnerLinkArray() {
		List<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", "hawai_bikes_accounts");
		map.put("value", Arrays.asList("name", "id"));
		arrayList.add(map);
		return arrayList;
	}

	@Override
	public boolean exists(UUID id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public long count() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(UUID id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(Bike entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(Iterable<? extends Bike> entities) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<IBike> findByOwner(IUser inCustomer) {
		int max_results = 1000;
		int returnDeleted = 0;
		List<Bike> bikes = getBikesOf(inCustomer);
		bikes = setOwner(inCustomer, bikes);
		bikes = addBikeType(bikes);
		return new ArrayList<IBike>(bikes);
	}

	private List<Bike> addBikeType(List<Bike> bikes) {
		for (Bike bike : bikes) {
			UUID bikeId = bike.getId();
			String session = connector.getSessionId();
			String module_name = "HAWAI_Bikes";
			UUID module_id = bikeId;
			String link_field_name = "hawai_bikes_aos_products";
			String related_module_query = "";
			List<String> related_fields = BikeTypeSerializationHelperSuite.getList();
			List<List<Map<String, Object>>> related_module_link_name_to_fields_array = new ArrayList<>();
			GetRelationshipsToken token = new GetRelationshipsToken(session, module_name, module_id,
					link_field_name, related_module_query, related_fields, related_module_link_name_to_fields_array);
			GetEntryListResponseToken responseToken = (GetEntryListResponseToken)
					connector.postGetRelationships(token, GetEntryListResponseToken.class);
			bike.setType(new BikeTypeResponseTokenDeserializerSuite().deserialize(responseToken).get(0));
		}
		return bikes;
	}

	private List<Bike> setOwner(IUser inCustomer, List<Bike> bikes) {
		for (Bike bike : bikes) {
			bike.setOwner(inCustomer);
		}
		return bikes;
	}

	private List<Bike> getBikesOf(IUser inCustomer) {
		String session = connector.getSessionId();
		String module_name = "Accounts";
		UUID module_id = inCustomer.getId();
		String link_field_name = "hawai_bikes_accounts";
		String related_module_query = "";
		List<String> related_fields = SELECT_FIELDS;
		List<List<Map<String, Object>>> related_module_link_name_to_fields_array = new ArrayList<>();
		GetRelationshipsToken token = new GetRelationshipsToken(session, module_name, module_id,
				link_field_name, related_module_query, related_fields, related_module_link_name_to_fields_array);
		GetEntryListResponseToken responseToken = (GetEntryListResponseToken)
				connector.postGetRelationships(token, GetEntryListResponseToken.class);
		List<Bike> bikes = new BikeResponseTokenDeserializerSuite().deserialize(responseToken);
		return bikes;
	}

	@Override
	public List<IBike> findBySoldLocation(ISellingLocation inSellingLocation) {
		throw new UnsupportedOperationException();
	}

}
