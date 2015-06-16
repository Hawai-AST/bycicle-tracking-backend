package de.hawai.bicycle_tracking.server.astcore.customermanagement.crm.suite;

import java.io.IOException;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.User;
import de.hawai.bicycle_tracking.server.utility.DateFormatUtil;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;

public class UserSerializerSuite extends JsonSerializer<User> {

	@Override
	public void serialize(User user, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeStartObject();
		addAttribute(jgen, UserSerializationHelperSuite.NAME, user.getName());
		addAttribute(jgen, UserSerializationHelperSuite.FIRSTNAME, user.getFirstName());
		EMail mailAddress = user.getMailAddress();
		if (mailAddress != null) {
			addAttribute(jgen, UserSerializationHelperSuite.EMAIL, mailAddress.getMailAddress());			
		}
		addAttribute(jgen, UserSerializationHelperSuite.PASSWORD, user.getPassword());
		UUID uuid = user.getId();
		if (uuid != null) {
			addAttribute(jgen, UserSerializationHelperSuite.UUID, uuid.toString());
		}
		if (user.getBirthdate() != null) {
			addAttribute(jgen, UserSerializationHelperSuite.BIRTHDAY, DateFormatUtil.DEFAULT_FORMAT.format(user.getBirthdate()));
		}
		Address address = user.getAddress();
		if (address != null) {
			addAttribute(jgen, UserSerializationHelperSuite.ADDRESS_STREET, address.getStreet());
//			TODO(fap): add house number
			addAttribute(jgen, UserSerializationHelperSuite.ADDRESS_CITY, address.getCity());
			addAttribute(jgen, UserSerializationHelperSuite.ADDRESS_POSTCODE, address.getPostcode());
			addAttribute(jgen, UserSerializationHelperSuite.ADDRESS_STATE, address.getState());
			addAttribute(jgen, UserSerializationHelperSuite.ADDRESS_COUNTRY, address.getCountry());
		}
		jgen.writeEndObject();
	}

	private void addAttribute(JsonGenerator jgen, String entryName, String value) throws IOException {
		jgen.writeObjectFieldStart(entryName);
		jgen.writeStringField("name", entryName);
		jgen.writeStringField("value", value);
		jgen.writeEndObject();
	}

}


