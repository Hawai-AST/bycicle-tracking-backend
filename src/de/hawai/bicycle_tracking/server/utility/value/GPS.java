package de.hawai.bicycle_tracking.server.utility.value;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GPS {

	private double longitude;
	private double latitude;

	public GPS(double longitude, double latitude) {
		this.setLongitude(longitude);
		this.setLatitude(latitude);
	}

	@Column(name = "gps_longitude", nullable = false)
	public double getLongitude() {
		return longitude;
	}

	private void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Column(name = "gps_latitude", nullable = false)
	public double getLatitude() {
		return latitude;
	}

	private void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GPS other = (GPS) obj;
		if (Double.doubleToLongBits(latitude) != Double
				.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double
				.doubleToLongBits(other.longitude))
			return false;
		return true;
	}

}
