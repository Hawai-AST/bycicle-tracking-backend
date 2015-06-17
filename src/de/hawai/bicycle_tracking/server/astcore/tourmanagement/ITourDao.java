package de.hawai.bicycle_tracking.server.astcore.tourmanagement;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.hawai.bicycle_tracking.server.astcore.bikemanagement.IBike;

@Repository
public interface ITourDao extends JpaRepository<Tour, Long> {
    Optional<ITour> getById(UUID id);
    @Query("SELECT t from Tour t where t.bike.id = :inBikeId")
    List<ITour> findByBikeId(@Param("inBikeId") UUID inBikeId);
}
