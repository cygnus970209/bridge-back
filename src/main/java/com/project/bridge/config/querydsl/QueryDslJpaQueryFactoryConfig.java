package com.project.bridge.config.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslJpaQueryFactoryConfig {

    @PersistenceContext(unitName = "bridgeEntityManager")
    private EntityManager bridgeEntityManager;

    @Bean
    public JPAQueryFactory bridgeJpaQueryFactory() {
        return new JPAQueryFactory(bridgeEntityManager);
    }
}
