package de.hawai.bicycle_tracking.server.dto;

import de.hawai.bicycle_tracking.server.utility.value.GPS;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TourDTO {
    public Long id = null;
    public String name = null;
    public Long bikeID = null;
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
