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
			date = new SimpleDateFormat("yyyy-MM-dd").parse(
					helper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.BIRTHDAY));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		User user = new User(helper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.NAME),
				helper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.FIRSTNAME),
				new EMail(helper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.EMAIL)),
				new Address(
						helper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.ADDRESS_STREET),
						"",
						helper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.ADDRESS_CITY),
						helper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.ADDRESS_STATE),
						helper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.ADDRESS_POSTCODE),
						helper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.ADDRESS_COUNTRY)),
				date,
				helper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.PASSWORD),
				new HawaiAuthority(helper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.AUTHORITY)),
				UUID.fromString(helper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.UUID)));
		return user;
	}
	
}
