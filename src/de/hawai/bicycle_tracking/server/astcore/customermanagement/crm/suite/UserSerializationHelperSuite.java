package de.hawai.bicycle_tracking.server.astcore.customermanagement.crm.suite;

import java.util.Arrays;
import java.util.List;

public class UserSerializationHelperSuite {

	public static final String NAME = "name";
	public static final String FIRSTNAME = "firstname_c";
	public static final String EMAIL = "email1";
	public static final String PASSWORD = "password_c";
	public static final String BIRTHDAY = "birthday_c";
	public static final String ADDRESS_STREET = "billing_address_street";
	public static final String ADDRESS_HOUSE_NUMBER = "billing_address_house_number_c";
	public static final String ADDRESS_CITY = "billing_address_city";
	public static final String ADDRESS_STATE = "billing_address_state";
	public static final String ADDRESS_POSTCODE = "billing_address_postalcode";
	public static final String ADDRESS_COUNTRY = "billing_address_country";
	public static final String AUTHORITY = "authority_c";
	public static final String UUID = "id";
	public static final String GENDER = "sex_c";
	
	public static List<String> getList() {
		return Arrays.asList(
				NAME, FIRSTNAME, EMAIL,
				PASSWORD, BIRTHDAY, 
				ADDRESS_STREET, ADDRESS_HOUSE_NUMBER,
				ADDRESS_CITY, ADDRESS_STATE,
				ADDRESS_POSTCODE, ADDRESS_COUNTRY,
				AUTHORITY, UUID, GENDER);
	}


}
