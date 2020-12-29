package com.ozragwort.moaon.springboot.component;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Component
public class StartupApplication implements ApplicationListener<ContextRefreshedEvent> {

    @PersistenceUnit
    EntityManagerFactory entityManagerFactory;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        try {
            fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
