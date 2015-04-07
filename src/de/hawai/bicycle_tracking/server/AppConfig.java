package de.hawai.bicycle_tracking.server;

import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@ComponentScan(basePackages = "de.hawai.bicycle_tracking.server")
@EnableJpaRepositories("de.hawai.bicycle_tracking.server")
@Import(DBConfig.class)
@Configuration
public class AppConfig {


}