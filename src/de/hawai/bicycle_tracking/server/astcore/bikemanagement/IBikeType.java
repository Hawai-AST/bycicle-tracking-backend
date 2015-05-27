package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import org.joda.time.Period;

import de.hawai.bicycle_tracking.server.utility.Entity;

public interface IBikeType extends Entity {
	
	public String getName();
	public String getDescription();
	public Period getInspectionInterval();
	
}
