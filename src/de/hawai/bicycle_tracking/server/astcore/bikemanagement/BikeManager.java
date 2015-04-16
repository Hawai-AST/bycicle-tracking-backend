package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.User;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;

public class BikeManager implements IBikeManager {

	@Autowired
	private BikeDao bikeDao;

	@Override
	public List<? extends IBike> findBikesBySoldLocation(SellingLocation inSellingLocation) {
		return bikeDao.findBySoldLocation(inSellingLocation);
	}

	@Override
	public IBike createBike(String inType, FrameNumber inFrameNumber, Date inBuyDate,
			Date inNextMaintenanceDate, SellingLocation inSellingLocation, User inOwner) {
		return bikeDao.save(new Bike(inType, inFrameNumber, inBuyDate, inNextMaintenanceDate, inSellingLocation, inOwner));
	}

}
