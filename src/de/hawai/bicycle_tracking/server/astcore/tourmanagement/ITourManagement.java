package de.hawai.bicycle_tracking.server.astcore.tourmanagement;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.utility.value.GPS;

public interface ITourManagement {
    public Optional<ITour> getTourById(UUID id);
    public List<ITour> getToursByUser(IUser user);
    public ITour addTour(String name, IBike bike, Date rodeAt, Date finishedAt, List<GPS> waypoints, double lengthInKm)
            throws AddTourFailedException;
    public void updateTour(ITour inTour, String name, IBike bike, Date rodeAt, Date finishedAt, List<GPS> waypoints, double lengthInKm)
            throws UpdateTourFailedException;
    public void deleteTour(ITour tour);
}
