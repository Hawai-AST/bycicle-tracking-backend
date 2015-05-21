package de.hawai.bicycle_tracking.server.astcore.tourmanagement;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import de.hawai.bicycle_tracking.server.utility.value.GPS;

public interface ITour {
    public UUID getId();
    public String getName();
    public IBike getBike();
    public Date getStartAt();
    public Date getFinishedAt();
    public Date getCreatedAt();
    public Date getUpdatedAt();
    public List<GPS> getWaypoints();
    public double getLengthInKm();
}
