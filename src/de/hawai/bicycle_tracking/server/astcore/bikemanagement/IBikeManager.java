package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import java.util.Date;
import java.util.List;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.User;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;

public interface IBikeManager {

	public List<? extends IBike> findBikesBySoldLocation(SellingLocation inSellingLocation);

	public IBike createBike(String inType, FrameNumber inFrameNumber, Date inBuyDate,
			Date inNextMaintenanceDate, SellingLocation inSellingLocation, User inOwner);

}
