package de.hawai.bicycle_tracking.server.astcore.bikemanagement.crm.suite;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.Bike;
import de.hawai.bicycle_tracking.server.crm.suite.SerializerHelper;
import de.hawai.bicycle_tracking.server.utility.DateFormatUtil;

public class BikeSerializerSuite extends JsonSerializer<Bike> {

	@Autowired
	private SerializerHelper helper;

	@Override
	public void serialize(Bike bike, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

		SerializerHelper helper = new SerializerHelper();
		jgen.writeStartObject();

		if (null != bike.getId()) {
			helper.addAttribute(jgen, BikeSerializationHelperSuite.UUID, bike.getId().toString());
		}
		if (null != bike.getName()) {
			helper.addAttribute(jgen, BikeSerializationHelperSuite.NAME, bike.getName());
		}
		if (null != bike.getFrameNumber()) {
			helper.addAttribute(jgen,
					BikeSerializationHelperSuite.FRAME_NUMBER,
					String.valueOf(
							bike.getFrameNumber().getNumber()));
		}
		if (null != bike.getNextMaintenance()) {
			helper.addAttribute(jgen, BikeSerializationHelperSuite.NEXT_MAINTENANCE_DATE,
					DateFormatUtil.DEFAULT_FORMAT.format(bike.getNextMaintenance()));
		}
		if (null != bike.getPurchaseDate()) {
			helper.addAttribute(jgen,
					BikeSerializationHelperSuite.PURCHASE_DATE,
					DateFormatUtil.DEFAULT_FORMAT.format(bike.getPurchaseDate()));
		}
		if (null != bike.getOwner() && null != bike.getOwner().getId()) {
			helper.addAttribute(jgen, BikeSerializationHelperSuite.OWNER, bike.getOwner().getId().toString());
		}

		if (null != bike.getType()) {
			helper.addAttribute(jgen,
					BikeSerializationHelperSuite.BIKE_TYPE,
					bike.getType().getId().toString());
		}

		//		helper.addAttribute(jgen, BikeSerializationHelperSuite.MILEAGE_IN_KM, bike.get);

		jgen.writeEndObject();
	}

}
