package de.hawai.bicycle_tracking.server.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IApplicationDao extends JpaRepository<Application, Integer> {
	Application getByClientID(String inClientID);
}
