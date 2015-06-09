package de.hawai.bicycle_tracking.server;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@ComponentScan(basePackages = { "de.hawai.bicycle_tracking.server" },
excludeFilters = { @Filter(type = FilterType.ANNOTATION, value = Configuration.class) })
@EnableJpaRepositories("de.hawai.bicycle_tracking.server")
@Import(DBConfig.class)
@Configuration
public class AppConfig implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (context.getEnvironment().getProperty("suite.enabled", "false").equals("true")) {
            registry.registerAlias("suiteUserDao", "userDAO");
        } else {
            registry.registerAlias("databaseUserDao", "userDAO");
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
