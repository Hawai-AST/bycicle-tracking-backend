package de.hawai.bicycle_tracking.server.astcore.bikemanagement.crm.suite;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.BikeType;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBikeTypeDao;
import de.hawai.bicycle_tracking.server.crm.suite.SuiteCrmConnector;
import de.hawai.bicycle_tracking.server.crm.suite.token.request.GetEntryListToken;
import de.hawai.bicycle_tracking.server.crm.suite.token.response.GetEntryListResponseToken;

public class BikeTypeDaoSuite implements IBikeTypeDao {
	
	private static final String MODULE = "AOS_Products";
	private static final List<String> SELECT_FIELDS = BikeTypeSerializationHelperSuite.getList();
	private SuiteCrmConnector connector;
	
	private BikeTypeDaoSuite() {
		super();
	}
	
	@Autowired
	public BikeTypeDaoSuite(SuiteCrmConnector connector) {
		super();
		this.connector = connector;
	}

	@Override
	public List<BikeType> findAll(Sort sort) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<BikeType> findAll(Iterable<UUID> ids) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends BikeType> List<S> save(Iterable<S> entities) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void flush() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends BikeType> S saveAndFlush(S entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteInBatch(Iterable<BikeType> entities) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAllInBatch() {
		throw new UnsupportedOperationException();
	}

	@Override
	public BikeType getOne(UUID id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Page<BikeType> findAll(Pageable pageable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <S extends BikeType> S save(S entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BikeType findOne(UUID id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean exists(UUID id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public long count() {
		return findAll().size();
	}

	@Override
	public void delete(UUID id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(BikeType entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(Iterable<? extends BikeType> entities) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<BikeType> findAll() {
		int max_results = 1000;
		int returnDeleted = 0;
//		TODO(fap): this will break if there are several product categories and not just bikes and only bikes
//		the only way to do this seems to be to call get_relationships for every(?!) entry in products?
//		maybe module_id can be empty?
//		http://developer.sugarcrm.com/2012/03/19/howto-avoiding-subqueries-with-our-web-services/
		String query = "";
		
//		write list return type (BikeTypeResponseToken) with List<BikeType>, this should be used for all queries
//		
//		better option: GetEntryResponseToken with List<String> that contain the entries of entry_list
//		then iterate over the entry list with class-specific deserializers
//		example response:
//		{"result_count":2,"total_count":"2","next_offset":2,
//		"entry_list":[{"id":"8fc04b4c-6c35-2079-4daa-555e056af2cb","module_name":"AOS_Products",
//		"name_value_list":{"id":{"name":"id","value":"8fc04b4c-6c35-2079-4daa-555e056af2cb"},"name":{"name":"name","value":"Cross Bike"}}},
//		{"id":"d3b44b8d-2e2a-be98-9db7-555de0e52688","module_name":"AOS_Products",
//		"name_value_list":{"id":{"name":"id","value":"d3b44b8d-2e2a-be98-9db7-555de0e52688"},"name":{"name":"name","value":"City Bike"}}}],
//		"relationship_list":[]}
		
		GetEntryListResponseToken responseToken = (GetEntryListResponseToken) connector.postGetEntryList(
				new GetEntryListToken(
						connector.getSessionId(), MODULE,
						query, max_results, returnDeleted, SELECT_FIELDS),
						GetEntryListResponseToken.class);
		
		return new BikeTypeResponseTokenDeserializerSuite().deserialize(responseToken);
	}



}
