package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import java.util.Date;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;

public interface IBike {

    public UUID getId();

    public BikeType getType();

    public Date getPurchaseDate();

    public FrameNumber getFrameNumber();

    public Date getNextMaintenanceDate();

    public ISellingLocation getSoldLocation();

    public IUser getOwner();
}
