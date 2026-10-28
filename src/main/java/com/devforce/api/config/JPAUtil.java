package com.devforce.api.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory FACTORY =
            Persistence.createEntityManagerFactory("api-clientes");

    private JPAUtil() {}

    public static EntityManager getEntityManager() {
        return FACTORY.createEntityManager();
    }

    public static void close() {
        FACTORY.close();
    }
}
