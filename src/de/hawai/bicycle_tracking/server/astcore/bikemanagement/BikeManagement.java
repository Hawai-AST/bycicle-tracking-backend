package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;


@Component
public class BikeManagement implements IBikeManagement {

	@Autowired
	@Qualifier("bikeDAO")
	private IBikeDao bikeDao;
	@Autowired
	@Qualifier("bikeTypeDAO")
	private IBikeTypeDao bikeTypeDao;
	@Autowired
	private ISellingLocationDao sellingLocationDao;

	@Override
	public List<? extends IBike> findBikesBySoldLocation(ISellingLocation inSellingLocation) {
		return bikeDao.findBySoldLocation(inSellingLocation);
	}

	@Override
	public IBike createBike(IBikeType inType, FrameNumber inFrameNumber, Date inPurchaseDate,
			Date inNextMaintenance, ISellingLocation inSellingLocation, IUser inOwner, String name) {
		return bikeDao.save(new Bike(inType, inFrameNumber, inPurchaseDate, inNextMaintenance, inSellingLocation, inOwner, name));
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
	public Optional<IBike> getBikeById(UUID inID) {
		return Optional.ofNullable(bikeDao.findOne(inID));
	}

	@Override
	public void updateBike(IBike inBike, IBikeType inType, FrameNumber inFrameNumber, Date inBuyDate,
						   Date inNextMaintenance, ISellingLocation inSellingLocation, IUser inOwner, String inName) {
		Bike old = (Bike) inBike;
		old.setPurchaseDate(inBuyDate);
		old.setFrameNumber(inFrameNumber);
		old.setNextMaintenance(inNextMaintenance);
		old.setType(inType);
		old.setOwner(inOwner);
		old.setSoldLocation(inSellingLocation);
		old.setName(inName);
		bikeDao.save(old);
	}

	@Override
	public List<IBikeType> getBikeTypes() {
		return new ArrayList<IBikeType>(bikeTypeDao.findAll());
	}

	@Override
	public Optional<IBikeType> getBikeTypeBy(UUID id) {
		return Optional.ofNullable((bikeTypeDao.findOne(id)));
	}
}
