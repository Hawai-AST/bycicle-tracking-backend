package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.User;
import de.hawai.bicycle_tracking.server.utility.AbstractEntity;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;

@Entity
public class Bike extends AbstractEntity implements IBike {
	private BikeType type;
	private FrameNumber frameNumber;
	private Date purchaseDate;
	private Date nextMaintenanceDate;
	private ISellingLocation soldLocation;
	private IUser owner;
	private String name;

	public Bike(final BikeType inType, final FrameNumber inFrameNumber, final Date inPurchaseDate, final Date inNextMaintenanceDate,
			final ISellingLocation inSellingLocation, final IUser inOwner, final String inName) {
		type = inType;
		frameNumber = inFrameNumber;
		purchaseDate = inPurchaseDate;
		nextMaintenanceDate = inNextMaintenanceDate;
		soldLocation = inSellingLocation;
		owner = inOwner;
		name = inName;
	}
	
	private Bike() {
		super();
	}

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bike_type_id")
	@Override
	public BikeType getType() {
		return type;
	}

	@Column(name = "frame_number", nullable = false, unique = true)
	@Override
	public FrameNumber getFrameNumber() {
		return frameNumber;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "purchase_date", nullable = false)
	@Override
	public Date getPurchaseDate() {
		return purchaseDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "next_maintenance")
	@Override
	public Date getNextMaintenanceDate() {
		return nextMaintenanceDate;
	}

	@ManyToOne(targetEntity = SellingLocation.class)
	@Override
	public ISellingLocation getSoldLocation() {
		return soldLocation;
	}

	@ManyToOne(targetEntity = User.class)
	@Override
	public IUser getOwner() {
		return this.owner;
	}
	
	@Column(name = "name", length = 30, nullable = false)
	public String getName() {
		return name;
	}

	public void setType(final BikeType inType) {
		type = inType;
	}

	private void setFrameNumber(final FrameNumber inFrameNumber) {
		frameNumber = inFrameNumber;
	}

	private void setPurchaseDate(final Date inPurchaseDate) {
		purchaseDate = inPurchaseDate;
	}

	private void setNextMaintenanceDate(final Date inNextMaintenanceDate) {
		nextMaintenanceDate = inNextMaintenanceDate;
	}

	private void setSoldLocation(final ISellingLocation inSoldLocation) {
		soldLocation = inSoldLocation;
	}

	public void setOwner(final IUser inOwner) {
		owner = inOwner;
	}

	private void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Bike [id=" + getId() + ", type=" + type + ", frameNumber=" + frameNumber +
				", purchaseDate=" + purchaseDate + ", nextMaintenanceDate="
				+ nextMaintenanceDate + ", soldLocation=" + soldLocation + ", owner=" + owner + "]";
	}

}
