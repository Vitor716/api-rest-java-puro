package com.devforce.api.repository;

import com.devforce.api.config.JPAUtil;
import com.devforce.api.model.Cliente;
import jakarta.persistence.EntityManager;

public class ClienteRepositoryImpl implements ClienteRepository {

    @Override
    public Cliente salvar(Cliente cliente) {
        try (EntityManager entityManager = JPAUtil.getEntityManager()) {
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(cliente);
                entityManager.getTransaction().commit();
                return cliente;
            } catch (RuntimeException e) {
                rollback(entityManager);
                throw e;
            }
        }
    }

    private void rollback(EntityManager entityManager) {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }
}
