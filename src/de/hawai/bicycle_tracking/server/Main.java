package de.hawai.bicycle_tracking.server;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.crm.suite.UserDaoSuite;
import de.hawai.bicycle_tracking.server.crm.suite.SuiteCrmConnector;
import de.hawai.bicycle_tracking.server.crm.suite.token.request.LoginToken;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ComponentScan
@EnableAutoConfiguration
@EnableWebMvc
@Configuration
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
