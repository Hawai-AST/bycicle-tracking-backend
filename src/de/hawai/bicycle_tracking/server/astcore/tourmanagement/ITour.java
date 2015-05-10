package de.hawai.bicycle_tracking.server.astcore.tourmanagement;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import de.hawai.bicycle_tracking.server.utility.Entity;
import de.hawai.bicycle_tracking.server.utility.value.GPS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface ITour {
    public Long getId();
    public String getName();
    public IBike getBike();
    public Date getRodeAt();
    public Date getFinishedAt();
    public Date getCreatedAt();
    public Date getUpdatedAt();
    public List<GPS> getWaypoints();
    public double getLengthInKm();
}
