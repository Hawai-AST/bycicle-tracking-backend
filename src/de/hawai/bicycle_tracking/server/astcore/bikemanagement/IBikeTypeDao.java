package de.hawai.bicycle_tracking.server.astcore.bikemanagement;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("databaseBikeTypeDao")
public interface IBikeTypeDao extends JpaRepository<BikeType, UUID> {

	@Override
	public List<BikeType> findAll();

}
