package de.hawai.bicycle_tracking.server.crm.suite.token;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonParseException;

//	 example of returned Json:
//	{
//		  "id": "51f481fa-f8af-9eda-cd5b-554deb40f6f8",
//		  "entry_list": {
//		    "name": {
//		      "name": "name",
//		      "value": "Louisa"
//		    },
//		    "description": {
//		      "name": "description",
//		      "value": "Hallo vom REST"
//		    }
//		  }
//		}

public class SetEntryResponseToken {
	
	private static final String UUID_REGEX_SUITE = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
	
	
	private String id;
	
	private String module_name;
	
	private HashMap<String, HashMap<String, String>> entry_list;
	
	public SetEntryResponseToken() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (isUuid(id)) {
			this.id = id;
		} else {
			throw new JsonParseException("Recieved String was not an UUID.");
		}
	}
	
	private boolean isUuid(String id2) {
		Pattern uuidPattern = Pattern.compile(UUID_REGEX_SUITE);
		Matcher uuidMatcher = uuidPattern.matcher(id2);
		return uuidMatcher.matches();
	}

	public String getModule_name() {
		return module_name;
	}
	
	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}
	
	public HashMap<String, HashMap<String, String>> getEntry_list() {
		return entry_list;
	}
	
	public void setEntry_list(HashMap<String, HashMap<String, String>> entry_list) {
		this.entry_list = entry_list;
	}

	@Override
	public String toString() {
		return "SetEntryResponseToken [id=" + id + ", moduleName=" + module_name + ", entryList=" + entry_list + "]";
	}

}
