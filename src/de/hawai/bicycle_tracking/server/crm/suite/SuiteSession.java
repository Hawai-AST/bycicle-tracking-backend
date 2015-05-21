package de.hawai.bicycle_tracking.server.crm.suite;

import java.util.Map;

public class SuiteSession {

	private String id;
	private String module_name;
	private Map<String, Map<String, String>> name_value_list;

	public String getSessionId() {
		return id;
	}

	public String getModule_name() {
		return module_name;
	}

	public Map<String, Map<String, String>> getName_value_list() {
		return name_value_list;
	}

	private String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}

	private void setModule_name(String module_name) {
		this.module_name = module_name;
	}

	private void setName_value_list(Map<String, Map<String, String>> name_value_list) {
		this.name_value_list = name_value_list;
	}

}
