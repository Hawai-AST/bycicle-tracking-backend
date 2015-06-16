package de.hawai.bicycle_tracking.server.astcore.bikemanagement.crm.suite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.Bike;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.User;
import de.hawai.bicycle_tracking.server.crm.suite.JsonParserHelper;
import de.hawai.bicycle_tracking.server.crm.suite.token.request.EntryListToken;
import de.hawai.bicycle_tracking.server.crm.suite.token.response.GetEntryListResponseToken;
import de.hawai.bicycle_tracking.server.utility.DateFormatUtil;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;

public class BikeResponseTokenDeserializerSuite {
	
	public BikeResponseTokenDeserializerSuite() {
		super();
	}

	public List<Bike> deserialize(GetEntryListResponseToken responseToken) {
		List<Bike> bikes = new ArrayList<Bike>();
		JsonParserHelper helper = new JsonParserHelper();
		for (int i = 0; i < responseToken.getEntry_list().size(); i++) {
			EntryListToken entryList = responseToken.getEntry_list().get(i);
			HashMap<String, HashMap<String, String>> nameValueList = entryList.getName_value_list();
			UUID id = UUID.fromString(helper.extractValueOf(nameValueList, BikeSerializationHelperSuite.UUID));
			FrameNumber frameNumber = new FrameNumber(
					Long.valueOf(helper.extractValueOf(nameValueList, BikeSerializationHelperSuite.FRAME_NUMBER)));
			Date purchaseDate = null;
			Date nextMaintenance = null;
			try {
				purchaseDate = DateFormatUtil.DEFAULT_FORMAT.parse(
						helper.extractValueOf(nameValueList, BikeSerializationHelperSuite.PURCHASE_DATE));
				nextMaintenance = DateFormatUtil.DEFAULT_FORMAT.parse(
						helper.extractValueOf(nameValueList, BikeSerializationHelperSuite.NEXT_MAINTENANCE_DATE));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			String name = helper.extractValueOf(nameValueList, BikeSerializationHelperSuite.NAME);
//			int mileage_in_km = Integer.valueOf(helper.extractValueOf(nameValueList, BikeSerializationHelperSuite.MILEAGE_IN_KM));
			User owner = getOwnerId(responseToken, helper, i);
			Bike bike = new Bike(null, frameNumber, purchaseDate, nextMaintenance, null, owner, name);
			bike.setId(id);
			bikes.add(bike);
		}
		return bikes;
	}

	private User getOwnerId(GetEntryListResponseToken responseToken, JsonParserHelper helper, int i) {
		if (!responseToken.getRelationship_list().isEmpty()) {
			
			HashMap<String, Object> relationships = responseToken.getRelationship_list().get(0);
			LinkedHashMap ownerMap = (LinkedHashMap)
					((LinkedHashMap)
							((ArrayList)
									((LinkedHashMap)
											((ArrayList) relationships.get("link_list")).get(i))
											.get("records"))
											.get(0))
											.get("link_value");
			return new User(null, null, null, null, null, null, null, UUID.fromString(helper.extractValueOf(ownerMap, "id")));
		}
		return null;
	}
	
	

}
