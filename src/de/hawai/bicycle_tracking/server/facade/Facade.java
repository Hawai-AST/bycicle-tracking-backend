package de.hawai.bicycle_tracking.server.facade;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import de.hawai.bicycle_tracking.server.utility.value.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBikeManagement;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBikeType;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.ISellingLocation;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.ICustomerManagement;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.astcore.tourmanagement.AddTourFailedException;
import de.hawai.bicycle_tracking.server.astcore.tourmanagement.ITour;
import de.hawai.bicycle_tracking.server.astcore.tourmanagement.ITourManagement;
import de.hawai.bicycle_tracking.server.astcore.tourmanagement.UpdateTourFailedException;

@Component
public class Facade {

	@Autowired
	private ICustomerManagement customerManagement;

	@Autowired
	private IBikeManagement bikeManagement;
	
	@Autowired
	private ITourManagement tourManagement;

	private Facade() {
		super();
	}

	public Optional<IUser> getUserBy(EMail eMail) {
		return customerManagement.getUserBy(eMail);
	}

	public IUser registerUser(String name, String firstName, EMail eMailAddress, Address address, Date birthdate, String password,
			  Gender gender, GrantedAuthority authority) {
		return customerManagement.registerUser(name, firstName, eMailAddress, address, birthdate, password, gender, authority);
	}

	public IUser registerUser(String name, String firstName, EMail eMailAddress, Address address, Date birthdate, String password,
							  GrantedAuthority authority) {
		return this.registerUser(name, firstName, eMailAddress, address, birthdate, password, Gender.NONE, authority);
	}


	public List<? extends IBike> findBikesBySoldLocation(ISellingLocation inLocation) {
		return this.bikeManagement.findBikesBySoldLocation(inLocation);
	}

	public IBike createBike(IBikeType inType, FrameNumber inFrameNumber, Date inBuyDate,
			Date inNextMaintenanceDate, ISellingLocation inSellingLocation, IUser inOwner, String name) {
		return this.bikeManagement.createBike(inType, inFrameNumber, inBuyDate, inNextMaintenanceDate, inSellingLocation, inOwner, name);
	}

	public List<? extends IBike> findByOwner(IUser inOwner) {
		return this.bikeManagement.findByOwner(inOwner);
	}

	public ISellingLocation createSellingLocation(Address inAddress, String inName) {
		return bikeManagement.createSellingLocation(inAddress, inName);
	}

	public Collection<? extends ISellingLocation> getAllSellingLocations() {
		return bikeManagement.getAllSellingLocations();
	}

	public Optional<IBike> getBikeById(UUID inID) {
		return bikeManagement.getBikeById(inID);
	}

	public void updateBike(IBike inBike, IBikeType iBikeType, FrameNumber inFrameNumber, Date inBuyDate,
						   Date inNextMaintenanceData, ISellingLocation inSellingLocation, IUser inOwner, String inName) {
		bikeManagement.updateBike(inBike, iBikeType, inFrameNumber, inBuyDate, inNextMaintenanceData, inSellingLocation, inOwner, inName);
	}
	
	public List<IBikeType> getBikeTypes() {
		return bikeManagement.getBikeTypes();
	}
	
	public Optional<IBikeType> getBikeTypeBy(UUID id) {
		return bikeManagement.getBikeTypeBy(id);
	}

	public void updatePassword(IUser user, String password) {
		this.customerManagement.updatePassword(user, password);
	}

	public void updateUser(IUser user, String name, String firstName, Date birthday, Address address) {
		customerManagement.updateUser(user, name, firstName, birthday, address);
	}

	public Optional<ITour> getTourById(UUID id) {
		return tourManagement.getTourById(id);
	}

	public ITour addTour(String name, IBike bike, Date rodeAt, Date finishedAt, List<GPS> waypoints, double lengthInKm)
			throws AddTourFailedException {
		return tourManagement.addTour(name, bike, rodeAt, finishedAt, waypoints, lengthInKm);
	}

	public void updateTour(ITour inTour, String name, IBike bike, Date rodeAt, Date finishedAt, List<GPS> waypoints, double lengthInKm)
			throws UpdateTourFailedException {
		tourManagement.updateTour(inTour, name, bike, rodeAt, finishedAt, waypoints, lengthInKm);
	}

	public List<ITour> getToursByUser(IUser user) {
		return tourManagement.getToursByUser(user);
	}

	public void deleteTour(ITour tour) {
		tourManagement.deleteTour(tour);
	}
}

