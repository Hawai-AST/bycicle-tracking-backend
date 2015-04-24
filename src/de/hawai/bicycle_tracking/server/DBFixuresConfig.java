package de.hawai.bicycle_tracking.server;

import de.hawai.bicycle_tracking.server.security.Application;
import de.hawai.bicycle_tracking.server.security.ApplicationDao;
import de.hawai.bicycle_tracking.server.security.HawaiAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Configuration
public class DBFixuresConfig {
	@Autowired
	private ApplicationDao applicationRepository;

	@PostConstruct
	public void setupApplication() {
		Application app = new Application("DEV-101", "DEVSECRET", Arrays.asList(HawaiAuthority.USER, HawaiAuthority.ADMIN), "read;write;track");
		this.applicationRepository.save(app);
	}
}
