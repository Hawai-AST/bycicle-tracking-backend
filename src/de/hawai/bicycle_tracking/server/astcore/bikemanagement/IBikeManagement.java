package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;

public interface IBikeManagement {

	public List<? extends IBike> findBikesBySoldLocation(ISellingLocation inSellingLocation);

	public List<? extends IBike> findByOwner(IUser inOwner);

	public IBike createBike(IBikeType inType, FrameNumber inFrameNumber, Date inBuyDate,
			Date inNextMaintenance, ISellingLocation inSellingLocation, IUser inOwner, String name);

	public ISellingLocation createSellingLocation(Address inAddress, String inName);

	public void updateBikesMileAge(IBike inBike, double mileAgeInKm);

	public Collection<? extends ISellingLocation> getAllSellingLocations();

	public Optional<IBike> getBikeById(UUID inID);

	public void updateBike(IBike inBike, IBikeType inType, FrameNumber inFrameNumber, Date inBuyDate,
						   Date inNextMaintenanceData, ISellingLocation inSellingLocation, IUser inOwner, String name);

	public List<IBikeType> getBikeTypes();

	public Optional<IBikeType> getBikeTypeBy(UUID id);

}
