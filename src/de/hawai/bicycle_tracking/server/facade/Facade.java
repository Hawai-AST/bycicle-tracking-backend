package de.hawai.bicycle_tracking.server.facade;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBikeManagement;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.ISellingLocation;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.ICustomerManagement;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class Facade {

	@Autowired
	private ICustomerManagement customerManagement;

	@Autowired
	private IBikeManagement bikeManagement;

	private Facade() {
		super();
	}

	public Optional<IUser> getUserBy(EMail eMail) {
		return customerManagement.getUserBy(eMail);
	}

	public IUser registerUser(String name, String firstName, EMail eMailAddress, Address address, Date birthdate, String password,
			  GrantedAuthority authority) {
		return customerManagement.registerUser(name, firstName, eMailAddress, address, birthdate, password, authority);
	}

	public List<? extends IBike> findBikesBySoldLocation(ISellingLocation inLocation) {
		return this.bikeManagement.findBikesBySoldLocation(inLocation);
	}

	public IBike createBike(String inType, FrameNumber inFrameNumber, Date inBuyDate,
			Date inNextMaintenanceDate, ISellingLocation inSellingLocation, IUser inOwner) {
		return this.bikeManagement.createBike(inType, inFrameNumber, inBuyDate, inNextMaintenanceDate, inSellingLocation, inOwner);
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

	public long getIdOfBike(IBike inBike) {
		return bikeManagement.getIdOfBike(inBike);
	}

	public IBike getBikeById(long inID) {
		return bikeManagement.getBikeById(inID);
	}

	public void updateBike(IBike inBike, String inType, FrameNumber inFrameNumber, Date inBuyDate,
						   Date inNextMaintenanceData, ISellingLocation inSellingLocation, IUser inOwner) {
		bikeManagement.updateBike(inBike, inType, inFrameNumber, inBuyDate, inNextMaintenanceData, inSellingLocation, inOwner);
	}
}
