package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface IBikeManagement {

	public List<? extends IBike> findBikesBySoldLocation(ISellingLocation inSellingLocation);

	public List<? extends IBike> findByOwner(IUser inOwner);

	public IBike createBike(String inType, FrameNumber inFrameNumber, Date inBuyDate,
			Date inNextMaintenanceDate, ISellingLocation inSellingLocation, IUser inOwner);

	public ISellingLocation createSellingLocation(Address inAddress, String inName);

	public Collection<? extends ISellingLocation> getAllSellingLocations();

	public long getIdOfBike(IBike inBike);

	public IBike getBikeById(long inID);

	public void updateBike(IBike inBike, String inType, FrameNumber inFrameNumber, Date inBuyDate,
						   Date inNextMaintenanceData, ISellingLocation inSellingLocation, IUser inOwner);
}
