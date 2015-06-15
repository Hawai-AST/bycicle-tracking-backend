package de.hawai.bicycle_tracking.server.astcore.customermanagement.crm.suite;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.User;
import de.hawai.bicycle_tracking.server.crm.suite.JsonParserHelper;
import de.hawai.bicycle_tracking.server.security.HawaiAuthority;
import de.hawai.bicycle_tracking.server.utility.exception.JsonParseException;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import de.hawai.bicycle_tracking.server.utility.value.Gender;

public class UserDeserializerSuite extends JsonDeserializer<User> {

	
	private static final String UUID_REGEX_SUITE = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

	@Override
	public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		JsonParserHelper helper = new JsonParserHelper();

		try {
			return deserializeFromGetEntryList(jsonParser, helper);
		} catch (JsonParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	private User deserializeFromGetEntryList(JsonParser jsonParser, JsonParserHelper helper)
			throws IOException, JsonProcessingException, JsonParseException {
		JsonNode node = helper.setupTreeparser(jsonParser);
		JsonNode nameValueList = null;
		try {
			nameValueList = helper.extractNameValueList(jsonParser, node);
			
		} catch (NoSuchElementException e) {
			System.err.println("Searched user doesn't exist.\n");
			return null;
		}
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(extractTextValueOf(nameValueList, UserSerializationHelperSuite.BIRTHDAY));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		User user = new User(extractTextValueOf(nameValueList, UserSerializationHelperSuite.NAME),
				extractTextValueOf(nameValueList, UserSerializationHelperSuite.FIRSTNAME),
				new EMail(extractTextValueOf(nameValueList, UserSerializationHelperSuite.EMAIL)),
				new Address(
						extractTextValueOf(nameValueList, UserSerializationHelperSuite.ADDRESS_STREET),
						"",
						extractTextValueOf(nameValueList, UserSerializationHelperSuite.ADDRESS_CITY),
						extractTextValueOf(nameValueList, UserSerializationHelperSuite.ADDRESS_STATE),
						extractTextValueOf(nameValueList, UserSerializationHelperSuite.ADDRESS_POSTCODE),
						extractTextValueOf(nameValueList, UserSerializationHelperSuite.ADDRESS_COUNTRY)),
				date,
				extractTextValueOf(nameValueList, UserSerializationHelperSuite.PASSWORD),
				Gender.byValue(extractTextValueOf(nameValueList, UserSerializationHelperSuite.GENDER)),
				new HawaiAuthority(extractTextValueOf(nameValueList, UserSerializationHelperSuite.AUTHORITY)),
				UUID.fromString(extractTextValueOf(nameValueList, UserSerializationHelperSuite.UUID)));
		return user;
	}

	private String extractTextValueOf(JsonNode nameValueList, String key) {
		return nameValueList.get(key).get("value").textValue();
	}
}
