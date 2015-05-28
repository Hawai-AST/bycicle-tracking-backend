package de.hawai.bicycle_tracking.server.dto;

import java.util.UUID;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBikeType;

public class BikeTypeDTO {

	private String name;
	private UUID id;
	private String description;
	private int inspectionIntervalInWeeks;

	public BikeTypeDTO(IBikeType bikeType) {
		this.name = bikeType.getName();
		this.id = bikeType.getId();
		this.description = bikeType.getDescription();
		this.inspectionIntervalInWeeks = bikeType.getInspectionInterval().toStandardWeeks().getWeeks();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getInspectionIntervalInWeeks() {
		return inspectionIntervalInWeeks;
	}
	public void setInspectionIntervalInWeeks(int inspectionIntervalInWeeks) {
		this.inspectionIntervalInWeeks = inspectionIntervalInWeeks;
	}

}
