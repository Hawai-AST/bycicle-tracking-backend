package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;

@Repository("databaseBikeDao")
public interface IBikeDao extends JpaRepository<Bike, UUID> {
    List<IBike> findByOwner(IUser inCustomer);

    @Query("SELECT bike from Bike bike WHERE bike.soldLocation = ?")
    List<IBike> findBySoldLocation(ISellingLocation inSellingLocation);
    
    public Bike findOne(UUID id);
}
