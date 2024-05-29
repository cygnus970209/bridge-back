package com.project.bridge.config.querydsl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class BridgeQueryDslRepositorySupport extends QuerydslRepositorySupport {

    public BridgeQueryDslRepositorySupport(Class<?> domainClass) {super(domainClass);}

    @Override
    @PersistenceContext(unitName = "bridgeEntityManager")
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
    }

}
