package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IBikeTypeDao extends JpaRepository<BikeType, Long> {
	
	public List<BikeType> findAll();

}
