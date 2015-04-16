package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginSessionDao extends JpaRepository<LoginSession, Long> {
}
