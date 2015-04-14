package de.hawai.bicycle_tracking.server.astcore.bikecomponent;

import java.util.Date;
import javax.persistence.*;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;

@Entity
public class Bike implements IBike
{
	private String type;
	private FrameNumber frameNumber;
	private Date buyDate;
	private Date nextMaintenanceDate;
	private SellingLocation soldLocation;

	public Bike(final String inType, final FrameNumber inFrameNumber, final Date inBuyDate, final Date inNextMaintenanceDate)
	{
		type = inType;
		frameNumber = inFrameNumber;
		buyDate = inBuyDate;
		nextMaintenanceDate = inNextMaintenanceDate;
	}

	@Column(name = "type", length = 50)
	@Override
	public String getType()
	{
		return type;
	}

	@Column(name = "frame_number", nullable = false)
	@Override
	public FrameNumber getFrameNumber()
	{
		return frameNumber;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "buy_date", nullable = false)
	@Override
	public Date getBuyDate()
	{
		return buyDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "next_maintenance")
	@Override
	public Date getNextMaintenanceDate()
	{
		return nextMaintenanceDate;
	}

	@ManyToOne
	@Column(name = "sold_location")
	@Override
	public SellingLocation getSoldLocation()
	{
		return soldLocation;
	}

	private void setType(final String inType)
	{
		type = inType;
	}

	private void setFrameNumber(final FrameNumber inFrameNumber)
	{
		frameNumber = inFrameNumber;
	}

	private void setBuyDate(final Date inBuyDate)
	{
		buyDate = inBuyDate;
	}

	private void setNextMaintenanceDate(final Date inNextMaintenanceDate)
	{
		nextMaintenanceDate = inNextMaintenanceDate;
	}
}
