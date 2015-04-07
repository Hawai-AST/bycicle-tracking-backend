package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationDao extends JpaRepository<Application, Integer>
{
	Application getByClientID(String inClientID);
}
