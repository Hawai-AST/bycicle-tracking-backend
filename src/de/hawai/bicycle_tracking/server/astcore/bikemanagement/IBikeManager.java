package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;

import java.util.Date;
import java.util.List;

public interface IBikeManager {

	public List<? extends IBike> findBikesBySoldLocation(ISellingLocation inSellingLocation);

	public List<? extends IBike> findByOwner(IUser inOwner);

	public IBike createBike(String inType, FrameNumber inFrameNumber, Date inBuyDate,
			Date inNextMaintenanceDate, ISellingLocation inSellingLocation, IUser inOwner);

}
