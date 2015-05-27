package de.hawai.bicycle_tracking.server.astcore.bikemanagement.crm.suite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.joda.time.Period;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.BikeType;
import de.hawai.bicycle_tracking.server.crm.suite.token.request.EntryListToken;
import de.hawai.bicycle_tracking.server.crm.suite.token.response.GetEntryListResponseToken;

public class BikeTypeResponseTokenDeserializerSuite {
	

	public List<BikeType> deserialize(GetEntryListResponseToken responseToken) {
		List<BikeType> bikeTypes = new ArrayList<>();
		for (EntryListToken entryList : responseToken.getEntry_list()) {
			HashMap<String, HashMap<String, String>> nameValueList = entryList.getName_value_list();
			Period period = Period.weeks(Integer.valueOf(
					extractValueOf(nameValueList, BikeTypeSerializationHelperSuite.INSPECTION_INTERVAL)));
			BikeType bikeType = new BikeType(extractValueOf(nameValueList, BikeTypeSerializationHelperSuite.NAME),
					extractValueOf(nameValueList, BikeTypeSerializationHelperSuite.DESCRIPTION),
					period);
			bikeType.setId(UUID.fromString(extractValueOf(nameValueList, BikeTypeSerializationHelperSuite.UUID)));
			bikeTypes.add(bikeType);
		}
		return bikeTypes;
	}
	
	private String extractValueOf(HashMap<String, HashMap<String, String>> nameValueList, String name) {
		return nameValueList.get(name).get("value");
	}

}
