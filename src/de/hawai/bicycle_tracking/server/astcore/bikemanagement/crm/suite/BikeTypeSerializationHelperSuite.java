package de.hawai.bicycle_tracking.server.astcore.bikemanagement.crm.suite;

import java.util.Arrays;
import java.util.List;

public class BikeTypeSerializationHelperSuite {
	
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String INSPECTION_INTERVAL = "inspection_interval_c";
	public static final String UUID = "id";
	
	public static List<String> getList() {
		return Arrays.asList(NAME,
				DESCRIPTION,
				INSPECTION_INTERVAL,
				UUID);
	}

}
