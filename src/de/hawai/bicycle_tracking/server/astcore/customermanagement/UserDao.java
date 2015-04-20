package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.hawai.bicycle_tracking.server.utility.value.EMail;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

	public Optional<User> getByName(String name);

	public Optional<User> getByMailAddress(EMail emailAddress);

}
