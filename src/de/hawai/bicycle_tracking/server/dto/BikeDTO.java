package de.hawai.bicycle_tracking.server.dto;

import java.util.UUID;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.BikeType;

public class BikeDTO {
    private UUID id;
    private String name;
    private long frameNumber;
    private UUID type;
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

    public UUID getType() {
        return type;
    }

    public void setType(UUID type) {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
