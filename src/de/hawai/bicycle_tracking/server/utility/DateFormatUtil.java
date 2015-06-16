package de.hawai.bicycle_tracking.server.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateFormatUtil {

	public static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final DateFormat TOUR_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
}
