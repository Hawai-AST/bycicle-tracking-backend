package de.hawai.bicycle_tracking.server.crm.suite.token.request;

import java.util.HashMap;
import java.util.UUID;

import de.hawai.bicycle_tracking.server.crm.suite.token.Token;

//"entry_list": [
//{
//"id": "8fc04b4c-6c35-2079-4daa-555e056af2cb",
//"module_name": "AOS_Products",
//"name_value_list": {
//  "id": {
//    "name": "id",
//    "value": "8fc04b4c-6c35-2079-4daa-555e056af2cb"
//  },
//  "name": {
//    "name": "name",
//    "value": "Cross Bike"
//  }
//}
//},
//{
//"id": "d3b44b8d-2e2a-be98-9db7-555de0e52688",
//"module_name": "AOS_Products",
//"name_value_list": {
//  "id": {
//    "name": "id",
//    "value": "d3b44b8d-2e2a-be98-9db7-555de0e52688"
//  },
//  "name": {
//    "name": "name",
//    "value": "City Bike"
//  }
//}
//}
//],

public class EntryListToken implements Token {
	
	private UUID id;
	private String module_name;
	private HashMap<String, HashMap<String, String>> name_value_list;
	
	public EntryListToken() {
		super();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getModule_name() {
		return module_name;
	}

	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}

	public HashMap<String, HashMap<String, String>> getName_value_list() {
		return name_value_list;
	}

	public void setName_value_list(HashMap<String, HashMap<String, String>> name_value_list) {
		this.name_value_list = name_value_list;
	}

	@Override
	public String toString() {
		return "EntryListToken [id=" + id + ", module_name=" + module_name + ", name_value_list=" + name_value_list + "]";
	}

}
