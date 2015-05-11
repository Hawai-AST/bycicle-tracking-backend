package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;

public interface IBikeManagement {

	public Optional<IBike> getBikeById(long id);

	public List<? extends IBike> findBikesBySoldLocation(ISellingLocation inSellingLocation);

	public List<? extends IBike> findByOwner(IUser inOwner);

	public IBike createBike(String inType, FrameNumber inFrameNumber, Date inBuyDate,
			Date inNextMaintenanceDate, ISellingLocation inSellingLocation, IUser inOwner);

	public ISellingLocation createSellingLocation(Address inAddress, String inName);

	public void updateBikesMileAge(IBike inBike, double mileAgeInKm);

}
