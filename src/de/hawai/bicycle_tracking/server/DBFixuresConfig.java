package de.hawai.bicycle_tracking.server;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.Application;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.ApplicationDao;

@Configuration
public class DBFixuresConfig {
	@Autowired
	private ApplicationDao applicationRepository;

	@PostConstruct
	public void setupApplication() {
		Application app = new Application();
		app.setClientID("DEV-101");
		this.applicationRepository.save(app);
	}
}
