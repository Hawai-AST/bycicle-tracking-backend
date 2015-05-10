package de.hawai.bicycle_tracking.server.astcore.tourmanagement;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.utility.value.GPS;

import java.util.Date;
import java.util.List;

public interface ITourManagement {
    public ITour getTourById(long id);
    public List<ITour> getToursByUser(IUser user);
    public ITour addTour(String name, IBike bike, Date rodeAt, Date finishedAt, List<GPS> waypoints, double lengthInKm);
    public void updateTour(ITour inTour, String name, IBike bike, Date rodeAt, Date finishedAt, List<GPS> waypoints, double lengthInKm);
    public void deleteTour(ITour tour);
}
