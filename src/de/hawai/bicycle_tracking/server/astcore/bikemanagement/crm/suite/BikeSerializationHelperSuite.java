package de.hawai.bicycle_tracking.server.astcore.bikemanagement.crm.suite;

import java.util.Arrays;
import java.util.List;


public class BikeSerializationHelperSuite {
	
	public static final String UUID = "id";
	public static final String NAME = "name";
	public static final String FRAME_NUMBER = "frame_number";
	public static final String PURCHASE_DATE = "purchase_date";
	public static final String NEXT_MAINTENANCE_DATE = "next_maintenance";
	public static final String MILEAGE_IN_KM = "mileage_in_km";
	public static final String OWNER = "hawai_bikes_accountsaccounts_ida";
	public static final String BIKE_TYPE = 	"hawai_bikes_aos_productsaos_products_ida";
	
	public static List<String> getList() {
		return Arrays.asList(
				PURCHASE_DATE,
				NEXT_MAINTENANCE_DATE,
				MILEAGE_IN_KM,
				FRAME_NUMBER,
				NAME,
				UUID,
				OWNER,
				BIKE_TYPE);
	}

}
