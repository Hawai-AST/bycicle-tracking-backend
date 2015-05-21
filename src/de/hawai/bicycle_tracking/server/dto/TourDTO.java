package de.hawai.bicycle_tracking.server.dto;

import java.util.List;
import java.util.UUID;

import de.hawai.bicycle_tracking.server.utility.value.GPS;

public class TourDTO {
    public UUID id = null;
    public String name = null;
    public UUID bikeID = null;
    public Double lengthInKm = null;
    public String startAt = null;
    public String finishedAt = null;
    public String createdAt = null;
    public String updatedAt = null;
    public List<GPS> waypoints = null;


    @Override
    public String toString() {
        return "TourDTO{" +
                "name='" + name + '\'' +
                ", bikeID=" + bikeID +
                ", lengthInKm=" + lengthInKm +
                ", rodeAt='" + startAt + '\'' +
                ", finishedAt='" + finishedAt + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", waypoints=" + waypoints +
                '}';
    }
}
