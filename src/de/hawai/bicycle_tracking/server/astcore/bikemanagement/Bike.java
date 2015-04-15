package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.User;
import de.hawai.bicycle_tracking.server.utility.AbstractEntity;
import de.hawai.bicycle_tracking.server.utility.value.FrameNumber;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Bike extends AbstractEntity implements IBike {
    private String type;
    private FrameNumber frameNumber;
    private Date buyDate;
    private Date nextMaintenanceDate;
    private SellingLocation soldLocation;
    private User owner;

    public Bike(final String inType, final FrameNumber inFrameNumber, final Date inBuyDate, final Date inNextMaintenanceDate, final SellingLocation inSellingLocation, final User inOwner) {
        type = inType;
        frameNumber = inFrameNumber;
        buyDate = inBuyDate;
        nextMaintenanceDate = inNextMaintenanceDate;
        soldLocation = inSellingLocation;
        owner = inOwner;
    }

    @Column(name = "type", length = 50)
    @Override
    public String getType() {
        return type;
    }

    @Column(name = "frame_number", nullable = false)
    @Override
    public FrameNumber getFrameNumber() {
        return frameNumber;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "buy_date", nullable = false)
    @Override
    public Date getBuyDate() {
        return buyDate;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "next_maintenance")
    @Override
    public Date getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    @ManyToOne
    @Override
    public SellingLocation getSoldLocation() {
        return soldLocation;
    }

    @ManyToOne
    @Override
    public User getOwner() {
        return this.owner;
    }

    private void setType(final String inType) {
        type = inType;
    }

    private void setFrameNumber(final FrameNumber inFrameNumber) {
        frameNumber = inFrameNumber;
    }

    private void setBuyDate(final Date inBuyDate) {
        buyDate = inBuyDate;
    }

    private void setNextMaintenanceDate(final Date inNextMaintenanceDate) {
        nextMaintenanceDate = inNextMaintenanceDate;
    }

    private void setSoldLocation(final SellingLocation inSoldLocation) {
        soldLocation = inSoldLocation;
    }

    private void setOwner(final User inOwner) {
        owner = inOwner;
    }
}
