package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Type;
import org.joda.time.Period;

import de.hawai.bicycle_tracking.server.utility.AbstractEntity;

@Entity
public class BikeType extends AbstractEntity {
	
	private String name;
	private String description;
	private Period inspectionInterval;
	
	private BikeType() {
		super();
	}

	public BikeType(String name, String description, Period inspectionInterval) {
		super();
		this.name = name;
		this.description = description;
		this.inspectionInterval = inspectionInterval;
	}

	@Column(name = "name", nullable = false, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", nullable = false, length = 250)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "inspection_interval")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentPeriodAsString")
	public Period getInspectionInterval() {
		return inspectionInterval;
	}

	public void setInspectionInterval(Period inspectionInterval) {
		this.inspectionInterval = inspectionInterval;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((inspectionInterval == null) ? 0 : inspectionInterval.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BikeType other = (BikeType) obj;
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (inspectionInterval == null) {
			if (other.inspectionInterval != null) {
				return false;
			}
		} else if (!inspectionInterval.equals(other.inspectionInterval)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "BikeType [id=" + getId() + ", name=" + name + ", description=" + description + 
				", inspectionInterval=" + inspectionInterval + "]";
	}

}
