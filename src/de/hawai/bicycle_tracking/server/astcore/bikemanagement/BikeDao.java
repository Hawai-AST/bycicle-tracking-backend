package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import java.util.List;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BikeDao extends JpaRepository<Bike, Long> {
    List<Bike> findByOwner(User inCustomer);

    @Query("SELECT bike from Bike bike WHERE bike.soldLocation = ?")
    List<Bike> findBySoldLocation(SellingLocation inSellingLocation);
}
