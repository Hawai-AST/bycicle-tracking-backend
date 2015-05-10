package de.hawai.bicycle_tracking.server.astcore.tourmanagement;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.utility.value.GPS;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ITourManagement {
    public Optional<ITour> getTourById(long id);
    public List<ITour> getToursByUser(IUser user);
    public ITour addTour(String name, IBike bike, Date rodeAt, Date finishedAt, List<GPS> waypoints, double lengthInKm)
            throws AddTourFailedException;
    public void updateTour(ITour inTour, String name, IBike bike, Date rodeAt, Date finishedAt, List<GPS> waypoints, double lengthInKm)
            throws UpdateTourFailedException;
    public void deleteTour(ITour tour);
}
