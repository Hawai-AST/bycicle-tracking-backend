package de.hawai.bicycle_tracking.server.utility.value;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GPS {

	private double longitude = -9000.0;
	private double latitude = -9000.0;
	private String name = null;

	private GPS(){};

	public GPS(double longitude, double latitude, String name) {
		this.setLongitude(longitude);
		this.setLatitude(latitude);
		this.setName(name);
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

	@Column(name = "gps_name", nullable = false)
	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GPS gps = (GPS) o;

		if (Double.compare(gps.longitude, longitude) != 0) return false;
		if (Double.compare(gps.latitude, latitude) != 0) return false;
		return !(name != null ? !name.equals(gps.name) : gps.name != null);

	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(longitude);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(latitude);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "GPS{" +
				"longitude=" + longitude +
				", latitude=" + latitude +
				", name='" + name + '\'' +
				'}';
	}
}
