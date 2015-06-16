package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
	private IBikeType type;
	private FrameNumber frameNumber;
	private Date purchaseDate;
	private Date nextMaintenance;
	private ISellingLocation soldLocation;
	private IUser owner;
	private double mileageInKm;
	private String name;

	public Bike(final IBikeType inType, final FrameNumber inFrameNumber, final Date inPurchaseDate, final Date inNextMaintenance,
			final ISellingLocation inSellingLocation, final IUser inOwner, final String inName) {
		type = inType;
		frameNumber = inFrameNumber;
		purchaseDate = inPurchaseDate;
		nextMaintenance = inNextMaintenance;
		soldLocation = inSellingLocation;
		owner = inOwner;
		mileageInKm = 0.0;
		name = inName;
	}
	
	private Bike() {
		super();
	}

	@ManyToOne(targetEntity = BikeType.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "bike_type_id")
	@Override
	public IBikeType getType() {
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
	public Date getNextMaintenance() {
		return nextMaintenance;
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
	
	@Override
	@Column(name = "mileage")
	public double getMileageInKm() {
		return mileageInKm;
	}
	
	@Override
	@Column(name = "name", length = 30, nullable = false)
	public String getName() {
		return name;
	}

	public void setType(final IBikeType inType) {
		type = inType;
	}

	protected void setFrameNumber(final FrameNumber inFrameNumber) {
		frameNumber = inFrameNumber;
	}

	protected void setPurchaseDate(final Date inPurchaseDate) {
		purchaseDate = inPurchaseDate;
	}

	protected void setNextMaintenance(final Date inNextMaintenance) {
		nextMaintenance = inNextMaintenance;
	}

	protected void setSoldLocation(final ISellingLocation inSoldLocation) {
		soldLocation = inSoldLocation;
	}

	public void setOwner(final IUser inOwner) {
		owner = inOwner;
	}
	
	public void setMileageInKm(double mileageInKm) {
		this.mileageInKm = mileageInKm;
	}

	protected void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Bike [id=" + getId() + ", type=" + type + ", frameNumber=" + frameNumber +
				", purchaseDate=" + purchaseDate + ", nextMaintenance="
				+ nextMaintenance + ", soldLocation=" + soldLocation + ", owner=" + owner + "]";
	}

}
