package de.hawai.bicycle_tracking.server.astcore.tourmanagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.Bike;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import de.hawai.bicycle_tracking.server.utility.AbstractEntity;
import de.hawai.bicycle_tracking.server.utility.value.GPS;

@Entity
@Table(name = "tour")
public class Tour extends AbstractEntity implements ITour {
    private static final long serialVersionUID = -875008919623232323L;
    private String name;
    private IBike bike;
    private Date startAt;
    private Date finishedAt;
    private Date createdAt;
    private Date updatedAt;
    private List<GPS> waypoints;
    private double lengthInKm;

    private Tour(){}

    protected Tour(String name, IBike bike, Date startAt, Date finishedAt, List<GPS> waypoints, double lengthInKm){
        this.name = name;
        this.bike = bike;
        this.startAt = startAt;
        this.finishedAt = finishedAt;
        this.waypoints = waypoints;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.lengthInKm = lengthInKm;
    }

    @Column(name = "name", nullable = false, length = 50)
    @Override
    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    @ManyToOne(targetEntity = Bike.class)
    @Override
    public IBike getBike() {
        return bike;
    }

    protected void setBike(IBike bike) {
        this.bike = bike;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "startAt", nullable = false)
    @Override
    public Date getStartAt() {
        return startAt;
    }

    protected void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "finshedAt", nullable = false)
    @Override
    public Date getFinishedAt() {
        return finishedAt;
    }

    protected void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "createdAt", nullable = false)
    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    private void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "updatedAt", nullable = false)
    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    protected void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @ElementCollection
    @Column(name = "waypoints", nullable = false)
    @Override
    public List<GPS> getWaypoints() {
        return new ArrayList<>(waypoints);
    }

    private void setWaypoints(List<GPS> waypoints) {
        this.waypoints = waypoints;
    }

    @Column(name = "lengthInKm", nullable = false)
    @Override
    public double getLengthInKm() {
        return lengthInKm;
    }

    private void setLengthInKm(double lengthInKm) {
        this.lengthInKm = lengthInKm;
    }

    protected void updateWay(List<GPS> waypoints, double lengthInKm){
        setWaypoints(new ArrayList<>(waypoints));
        setLengthInKm(lengthInKm);
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tour other = (Tour) obj;
        return other.getId() == this.getId();
    }

    @Override
    public int hashCode() {
        return (int) (getId().hashCode() ^ (getId().hashCode() >>> 32));
    }
}
