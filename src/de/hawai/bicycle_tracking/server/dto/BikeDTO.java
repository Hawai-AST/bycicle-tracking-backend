package de.hawai.bicycle_tracking.server.dto;

import java.util.UUID;

public class BikeDTO {
    private UUID id;
    private long frameNumber;
    private String type;
    private String salesLocation;
    private String purchaseDate;
    private String nextMaintenance;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(long frameNumber) {
        this.frameNumber = frameNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSalesLocation() {
        return salesLocation;
    }

    public void setSalesLocation(String salesLocation) {
        this.salesLocation = salesLocation;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getNextMaintenance() {
        return nextMaintenance;
    }

    public void setNextMaintenance(String nextMaintenance) {
        this.nextMaintenance = nextMaintenance;
    }
}
