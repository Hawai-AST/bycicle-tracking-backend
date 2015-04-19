package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class BikeManager implements IBikeManager {

	@Autowired
	private BikeDao bikeDao;

	@Override
	public List<? extends IBike> findBikesBySoldLocation(ISellingLocation inSellingLocation) {
		return bikeDao.findBySoldLocation(inSellingLocation);
	}

	@Override
	public IBike createBike(String inType, FrameNumber inFrameNumber, Date inBuyDate,
			Date inNextMaintenanceDate, ISellingLocation inSellingLocation, IUser inOwner) {
		return bikeDao.save(new Bike(inType, inFrameNumber, inBuyDate, inNextMaintenanceDate, inSellingLocation, inOwner));
	}

	@Override
	public List<? extends IBike> findByOwner(IUser inOwner) {
		return this.bikeDao.findByOwner(inOwner);
	}
}
