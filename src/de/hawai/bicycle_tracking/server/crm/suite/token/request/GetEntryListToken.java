package de.hawai.bicycle_tracking.server.crm.suite.token.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.hawai.bicycle_tracking.server.crm.suite.token.Token;

public class GetEntryListToken implements Token {

	private String session;
	private String module_name;
	private String query;
	private final String order_by;
	private final int offset;
	private List<String> select_fields;
	private List<HashMap<String, Object>> link_name_to_fields_array;
	private final int max_results;
	private int deleted;
	private final boolean favorites;

	public GetEntryListToken(String session, String module_name, String query,
			int max_results, int deleted, List<String> select_fields) {
		this.session = session;
		this.module_name = module_name;
		this.query = query;
		this.deleted = deleted;
		this.order_by = "";
		this.offset = 0;

		this.link_name_to_fields_array = new ArrayList<>();
		this.max_results = max_results;
		this.favorites = false;
		this.select_fields = select_fields;
	}
	
	public void setLinkNameArray(List<HashMap<String, Object>> getOwnerLinkArray) {
		this.link_name_to_fields_array = getOwnerLinkArray;
	}

}
