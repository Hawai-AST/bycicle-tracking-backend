package de.hawai.bicycle_tracking.server.crm.suite.token.request;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hawai.bicycle_tracking.server.crm.suite.token.Token;

public class GetRelationshipsToken implements Token {
	
	private String session;
	private String module_name;
	private UUID module_id;
	private String link_field_name;
	private String related_module_query;
	private List<String> related_fields;
	private List<List<Map<String, Object>>> related_module_link_name_to_fields_array;
	private int deleted;
	private String order_by;
	private int offset;
	private int limit;

	public GetRelationshipsToken(String session, String module_name, UUID module_id, String link_field_name,
			String related_module_query, List<String> related_fields,
			List<List<Map<String, Object>>> related_module_link_name_to_fields_array2,
			int deleted, String order_by, int offset, int limit) {
		this.session = session;
		this.module_name = module_name;
		this.module_id = module_id;
		this.link_field_name = link_field_name;
		this.related_module_query = related_module_query;
		this.related_fields = related_fields;
		this.related_module_link_name_to_fields_array = related_module_link_name_to_fields_array2;
		this.deleted = deleted;
		this.order_by = order_by;
		this.offset = offset;
		this.limit = limit;
	}
	
	public GetRelationshipsToken(String session, String module_name, UUID module_id, String link_field_name,
			String related_module_query, List<String> related_fields,
			List<List<Map<String, Object>>> related_module_link_name_to_fields_array2) {
		this(session, module_name, module_id, link_field_name,
				related_module_query, related_fields, related_module_link_name_to_fields_array2, 0, "", 0, 0);
	}

}
