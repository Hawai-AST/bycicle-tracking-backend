package de.hawai.bicycle_tracking.server.astcore.bikemanagement.crm.suite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.Bike;
import de.hawai.bicycle_tracking.server.crm.suite.JsonParserHelper;
import de.hawai.bicycle_tracking.server.crm.suite.token.request.EntryListToken;
import de.hawai.bicycle_tracking.server.crm.suite.token.response.GetEntryListResponseToken;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;

public class BikeResponseTokenDeserializerSuite {
	
	public BikeResponseTokenDeserializerSuite() {
		super();
	}

	public List<Bike> deserialize(GetEntryListResponseToken responseToken) {
		List<Bike> bikes = new ArrayList<Bike>();
		JsonParserHelper helper = new JsonParserHelper();
		for (EntryListToken entryList : responseToken.getEntry_list()) {
			HashMap<String, HashMap<String, String>> nameValueList = entryList.getName_value_list();
			UUID id = UUID.fromString(helper.extractValueOf(nameValueList, BikeSerializationHelperSuite.UUID));
			FrameNumber frameNumber = new FrameNumber(
					Long.valueOf(helper.extractValueOf(nameValueList, BikeSerializationHelperSuite.FRAME_NUMBER)));
			Date purchaseDate = null;
			Date nextMaintenanceDate = null;
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				purchaseDate = simpleDateFormat.parse(
						helper.extractValueOf(nameValueList, BikeSerializationHelperSuite.PURCHASE_DATE));
				nextMaintenanceDate = simpleDateFormat.parse(
						helper.extractValueOf(nameValueList, BikeSerializationHelperSuite.NEXT_MAINTENANCE_DATE));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			String name = helper.extractValueOf(nameValueList, BikeSerializationHelperSuite.NAME);
//			int mileage_in_km = Integer.valueOf(helper.extractValueOf(nameValueList, BikeSerializationHelperSuite.MILEAGE_IN_KM));
			Bike bike = new Bike(null, frameNumber, purchaseDate, nextMaintenanceDate, null, null, name);
			bike.setId(id);
			bikes.add(bike);
		}
		return bikes;
	}
	
	

}
