package de.hawai.bicycle_tracking.server.utility.test;

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil {
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}
	public static <E extends Object> E jsonToObject(Class<E> inClass, String json){
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, inClass);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
