package de.hawai.bicycle_tracking.server.astcore.customermanagement.crm.suite;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.User;
import de.hawai.bicycle_tracking.server.crm.suite.JsonParserHelper;
import de.hawai.bicycle_tracking.server.crm.suite.SerializerHelper;
import de.hawai.bicycle_tracking.server.security.HawaiAuthority;
import de.hawai.bicycle_tracking.server.utility.DateFormatUtil;
import de.hawai.bicycle_tracking.server.utility.exception.JsonParseException;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import de.hawai.bicycle_tracking.server.utility.value.Gender;

public class UserDeserializerSuite extends JsonDeserializer<User> {

	private JsonParserHelper parserHelper;
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
			String birthdate = helper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.BIRTHDAY);
			if (null != birthdate) {
				date = DateFormatUtil.DEFAULT_FORMAT.parse(birthdate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JsonParserHelper parserHelper = new JsonParserHelper();
		User user = new User(parserHelper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.NAME),
				parserHelper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.FIRSTNAME),
				new EMail(parserHelper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.EMAIL)),
				new Address(
						parserHelper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.ADDRESS_STREET),
						parserHelper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.ADDRESS_HOUSE_NUMBER),
						parserHelper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.ADDRESS_CITY),
						parserHelper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.ADDRESS_STATE),
						parserHelper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.ADDRESS_POSTCODE),
						parserHelper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.ADDRESS_COUNTRY)),
				date,
				parserHelper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.PASSWORD),
				Gender.byValue(parserHelper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.GENDER)),
				new HawaiAuthority(parserHelper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.AUTHORITY)),
				UUID.fromString(parserHelper.extractTextValueOf(nameValueList, UserSerializationHelperSuite.UUID)));
		return user;
	}
	
}
