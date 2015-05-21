package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import java.util.Date;
import java.util.UUID;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;

public interface IBike {

    public UUID getId();

    public String getType();

    public Date getPurchaseDate();

    public FrameNumber getFrameNumber();

    public Date getNextMaintenance();

    public ISellingLocation getSoldLocation();

    public IUser getOwner();

    public double getMileageInKm();
}
