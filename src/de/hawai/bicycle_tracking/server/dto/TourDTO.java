package de.hawai.bicycle_tracking.server.dto;

import de.hawai.bicycle_tracking.server.utility.value.GPS;

import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 10.05.2015.
 */
public class TourDTO {
    public String name;
    public long bikeID;
    public double lengthInKm;
    public String rodeAt;
    public String finishedAt;
    public List<GPS> waypoints;

    @Override
    public String toString() {
        return "TourDTO{" +
                "name='" + name + '\'' +
                ", bikeID=" + bikeID +
                ", lengthInKm=" + lengthInKm +
                ", rodeAt='" + rodeAt + '\'' +
                ", finishedAt='" + finishedAt + '\'' +
                ", waypoints=" + waypoints +
                '}';
    }
}
