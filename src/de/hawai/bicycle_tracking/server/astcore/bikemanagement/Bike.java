package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.User;
import de.hawai.bicycle_tracking.server.utility.AbstractEntity;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;

@Entity
public class Bike extends AbstractEntity implements IBike {
	private String type;
	private FrameNumber frameNumber;
	private Date purchaseDate;
	private Date nextMaintenance;
	private ISellingLocation soldLocation;
	private IUser owner;

	protected Bike(final String inType, final FrameNumber inFrameNumber, final Date inBuyDate, final Date inNextMaintenanceDate,
			final ISellingLocation inSellingLocation, final IUser inOwner) {
		type = inType;
		frameNumber = inFrameNumber;
		purchaseDate = inBuyDate;
		nextMaintenance = inNextMaintenanceDate;
		soldLocation = inSellingLocation;
		owner = inOwner;
	}

	protected Bike() {
	}

	@Column(name = "type", length = 50)
	@Override
	public String getType() {
		return type;
	}

	@Column(name = "frame_number", nullable = false, unique = true)
	@Override
	public FrameNumber getFrameNumber() {
		return frameNumber;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "buy_date", nullable = false)
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

	protected void setType(final String inType) {
		type = inType;
	}

	protected void setFrameNumber(final FrameNumber inFrameNumber) {
		frameNumber = inFrameNumber;
	}

	protected void setPurchaseDate(final Date inBuyDate) {
		purchaseDate = inBuyDate;
	}

	protected void setNextMaintenance(final Date inNextMaintenance) {
		nextMaintenance = inNextMaintenance;
	}

	protected void setSoldLocation(final ISellingLocation inSoldLocation) {
		soldLocation = inSoldLocation;
	}

	protected void setOwner(final IUser inOwner) {
		owner = inOwner;
	}
}
