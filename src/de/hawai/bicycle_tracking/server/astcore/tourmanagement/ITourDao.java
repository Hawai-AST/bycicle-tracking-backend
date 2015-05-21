package de.hawai.bicycle_tracking.server.astcore.tourmanagement;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITourDao extends JpaRepository<Tour, Long> {
    Optional<ITour> getById(long id);
    List<ITour> findByBike(IBike inBike);
}
