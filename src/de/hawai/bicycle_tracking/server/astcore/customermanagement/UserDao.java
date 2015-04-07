package de.hawai.bicycle_tracking.server.astcore.customermanagement;

import de.hawai.bicycle_tracking.server.utility.value.EMail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

	public User getByName(String name);

	User getByeMailAddress(EMail emailAddress);

}
