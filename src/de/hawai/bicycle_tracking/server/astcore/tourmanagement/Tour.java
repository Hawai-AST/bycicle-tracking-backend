package de.hawai.bicycle_tracking.server.astcore.tourmanagement;

import com.sun.corba.se.impl.orb.PropertyOnlyDataCollector;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.Bike;
import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.User;
import de.hawai.bicycle_tracking.server.utility.AbstractEntity;
import de.hawai.bicycle_tracking.server.utility.value.GPS;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tour")
public class Tour extends AbstractEntity implements ITour {
    private static final long serialVersionUID = -875008919623232323L;
    private String name;
    private IBike bike;
    private Date rodeAt;
    private Date finishedAt;
    private Date createdAt;
    private Date updatedAt;
    private List<GPS> waypoints;
    private double lengthInKm;

    protected Tour(String name, IBike bike, Date rodeAt, Date finishedAt, List<GPS> waypoints, double lengthInKm){
        this.name = name;
        this.bike = bike;
        this.rodeAt = rodeAt;
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
    @Column(name = "rodeAt", nullable = false)
    @Override
    public Date getRodeAt() {
        return rodeAt;
    }

    protected void setRodeAt(Date rodeAt) {
        this.rodeAt = rodeAt;
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
        return (int) (getId() ^ (getId() >>> 32));
    }
}
