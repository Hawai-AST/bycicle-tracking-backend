package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBikeDao extends JpaRepository<Bike, Long> {
    List<IBike> findByOwner(IUser inCustomer);

    @Query("SELECT bike from Bike bike WHERE bike.soldLocation = ?")
    List<IBike> findBySoldLocation(ISellingLocation inSellingLocation);
}
