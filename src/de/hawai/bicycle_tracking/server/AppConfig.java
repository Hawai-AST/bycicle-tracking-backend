package de.hawai.bicycle_tracking.server;

import de.hawai.bicycle_tracking.server.astcore.customermanagement.IUserDao;
import de.hawai.bicycle_tracking.server.astcore.customermanagement.crm.suite.UserDaoSuite;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;

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
        if (context.getEnvironment().getProperty("suite.enabled").equals("true")) {
            registry.registerAlias("suiteRepository", "beepbeep");
        } else {
            registry.registerAlias("databaseUserDao", "beepbeep");
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
