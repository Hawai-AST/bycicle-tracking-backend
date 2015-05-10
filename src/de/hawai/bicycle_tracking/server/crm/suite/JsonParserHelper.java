package de.hawai.bicycle_tracking.server.crm.suite;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonParseException;

@Component
public class JsonParserHelper {
	
	public JsonParserHelper() {
		super();
	}
	
	public JsonNode extractNameValueList(JsonParser jsonParser, JsonNode node) throws IOException, JsonProcessingException {
		JsonNode entryListArray = node.get("entry_list");
		JsonNode name_value_list = null;
		if (entryListArray.isArray()) {
			JsonNode entryList = entryListArray.elements().next();
			name_value_list = entryList.get("name_value_list");
		} else {
			throw new JsonParseException("");
		}
		return name_value_list;
	}

	public JsonNode setupTreeparser(JsonParser jsonParser) throws IOException, JsonProcessingException {
		ObjectCodec oc = jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser);
		return node;
	}

}
