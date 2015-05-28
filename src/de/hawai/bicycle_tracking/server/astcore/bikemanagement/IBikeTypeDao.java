package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IBikeTypeDao extends JpaRepository<BikeType, UUID> {

	@Override
	public List<BikeType> findAll();

}
