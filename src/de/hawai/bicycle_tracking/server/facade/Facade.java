package de.hawai.bicycle_tracking.server.facade;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBikeManager;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.ISellingLocation;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.ICustomerManagement;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.utility.value.Address;
import de.hawai.bicycle_tracking.server.utility.value.EMail;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class Facade {

	@Autowired
	private ICustomerManagement customerManegement;

	@Autowired
	private IBikeManager bikeManagement;

	private Facade() {
		super();
	}

	public Optional<IUser> getUserBy(EMail eMail) {
		return customerManegement.getUserBy(eMail);
	}

	public IUser registerUser(String name, String firstName, EMail eMailAddress, Address address, Date birthdate, String password) {
		return customerManegement.registerUser(name, firstName, eMailAddress, address, birthdate, password);
	}

	public List<? extends IBike> findBikesBySoldLocation(ISellingLocation inLocation) {
		return this.bikeManagement.findBikesBySoldLocation(inLocation);
	}

	public IBike createBike(String inType, FrameNumber inFrameNumber, Date inBuyDate, Date inNextMaintenanceDate, ISellingLocation inSellingLocation, IUser inOwner) {
		return this.bikeManagement.createBike(inType, inFrameNumber, inBuyDate, inNextMaintenanceDate, inSellingLocation, inOwner);
	}

	public List<? extends IBike> findByOwner(IUser inOwner) {
		return this.bikeManagement.findByOwner(inOwner);
	}
}
