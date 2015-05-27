package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;


@Component
public class BikeManagement implements IBikeManagement {

	@Autowired
	private IBikeDao bikeDao;
	@Autowired
	private ISellingLocationDao sellingLocationDao;

	@Override
	public List<? extends IBike> findBikesBySoldLocation(ISellingLocation inSellingLocation) {
		return bikeDao.findBySoldLocation(inSellingLocation);
	}

	@Override
	public IBike createBike(BikeType inType, FrameNumber inFrameNumber, Date inPurchaseDate,
			Date inNextMaintenanceDate, ISellingLocation inSellingLocation, IUser inOwner, String name) {
		return bikeDao.save(new Bike(inType, inFrameNumber, inPurchaseDate, inNextMaintenanceDate, inSellingLocation, inOwner, name));
	}

	@Override
	public List<? extends IBike> findByOwner(IUser inOwner) {
		return this.bikeDao.findByOwner(inOwner);
	}

	@Override
	public ISellingLocation createSellingLocation(Address inAddress, String inName) {
		return sellingLocationDao.save(new SellingLocation(inAddress, inName));
	}

	@Override
	public void updateBikesMileAge(IBike inBike, double mileAgeInKm) {
		Bike bike = (Bike) inBike;
		bike.setMileageInKm(mileAgeInKm);
		bikeDao.save(bike);
	}

	public Collection<? extends ISellingLocation> getAllSellingLocations() {
		return sellingLocationDao.findAll();
	}

	@Override
	public UUID getIdOfBike(IBike inBike) {
		if (inBike instanceof Bike) {
			return ((Bike) inBike).getId();
		} else {
			return null;
		}
	}

	@Override
	public Optional<IBike> getBikeById(UUID inID) {
		return Optional.ofNullable(bikeDao.findOne(inID));
	}

	@Override
	public void updateBike(IBike inBike, BikeType inType, FrameNumber inFrameNumber, Date inBuyDate,
						   Date inNextMaintenanceData, ISellingLocation inSellingLocation, IUser inOwner, String inName) {
		Bike old = (Bike) inBike;
		old.setPurchaseDate(inBuyDate);
		old.setFrameNumber(inFrameNumber);
		old.setNextMaintenanceDate(inNextMaintenanceData);
		old.setType(inType);
		old.setOwner(inOwner);
		old.setSoldLocation(inSellingLocation);
		old.setName(inName);
		bikeDao.save(old);
	}
}
