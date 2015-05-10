package de.hawai.bicycle_tracking.server.astcore.tourmanagement;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITourDao extends JpaRepository<Tour, Long> {
    List<ITour> findByBike(IBike inBike);
}
