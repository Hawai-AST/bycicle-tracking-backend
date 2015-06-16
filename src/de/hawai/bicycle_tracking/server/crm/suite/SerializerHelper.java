package de.hawai.bicycle_tracking.server.crm.suite;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;

public class SerializerHelper {
	
	public SerializerHelper() {
		super();
	}
	
	public void addAttribute(JsonGenerator jgen, String entryName, String value) throws IOException {
		jgen.writeObjectFieldStart(entryName);
		jgen.writeStringField("name", entryName);
		jgen.writeStringField("value", value);
		jgen.writeEndObject();
	}

}
