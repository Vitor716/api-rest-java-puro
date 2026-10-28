package com.devforce.api;

import com.devforce.api.config.JPAUtil;
import com.devforce.api.controller.ClienteController;
import com.devforce.api.repository.ClienteRepository;
import com.devforce.api.repository.ClienteRepositoryImpl;
import com.devforce.api.service.ClienteService;
import com.sun.net.httpserver.HttpServer;
import jakarta.persistence.EntityManager;

import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws Exception {
        System.setProperty("org.jboss.logging.provider", "slf4j");

        try (EntityManager entityManager = JPAUtil.getEntityManager()) {
            entityManager.createNativeQuery("SELECT 1").getSingleResult();
        } catch (Throwable e) {
            System.err.println("Não foi possível conectar ao banco. O container está rodando? (docker compose up -d)");
            throw e;
        }

        ClienteRepository repository = new ClienteRepositoryImpl();
        ClienteService service = new ClienteService(repository);
        ClienteController controller = new ClienteController(service);

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/clientes", controller);
        server.start();

        System.out.println("API iniciada na porta 8080");
    }
}
