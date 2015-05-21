package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.hawai.bicycle_tracking.server.utility.value.EMail;

@Primary
@Repository
public interface IUserDao extends JpaRepository<User, UUID> {

	public Optional<User> getByName(String name);

	public Optional<User> getByMailAddress(EMail emailAddress);

}
