package com.project.bridge.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.project.bridge.repositories",
        entityManagerFactoryRef = "bridgeEntityManagerFactory",
        transactionManagerRef = "bridgeTransactionManager"
)
@EntityScan("com.project.bridge.entity")
public class BridgeDataSourceConfiguration {

    @Value("${spring.datasource.username}")
    private String USERNAME;

    @Value("${spring.datasource.password}")
    private String PASSWORD;

    @Value("${spring.datasource.url}")
    private String URL;

    @Value("${spring.datasource.driver-class-name}")
    private String DRIVERNAME;

    @Primary
    @Bean(name = "bridgeDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource bridgeDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .username(USERNAME)
                .password(PASSWORD)
                .url(URL)
                .driverClassName(DRIVERNAME)
                .build();
    }

    @Primary
    @Bean(name = "bridgeEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean bridgeEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("bridgeDataSource") DataSource dataSource) {
        return builder.dataSource(dataSource).packages("com.project.bridge.entity").persistenceUnit("bridgeEntityManager").build();
    }

    @Primary
    @Bean(name = "bridgeTransactionManager")
    public PlatformTransactionManager bridgeTransactionManager(@Qualifier("bridgeEntityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory.getObject());
        transactionManager.setNestedTransactionAllowed(true);
        return transactionManager;
    }
}
