package com.devforce.api.controller;

import com.devforce.api.model.Cliente;
import com.devforce.api.service.ClienteService;
import com.devforce.api.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.NoSuchElementException;

import static com.devforce.api.util.RepostaUtil.*;

public class ClienteController implements HttpHandler {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            rotear(exchange);
        } catch (NumberFormatException e) {
            responder(exchange, 400, erro("ID inválido"));
        } catch (JsonProcessingException e) {
            responder(exchange, 400, erro("JSON inválido"));
        } catch (IllegalArgumentException e) {
            responder(exchange, 400, erro(e.getMessage()));
        } catch (NoSuchElementException e) {
            responder(exchange, 404, erro(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            responder(exchange, 500, erro("Erro interno"));
        }
    }

    private void rotear(HttpExchange exchange) throws IOException {
        String metodo = exchange.getRequestMethod();
        String[] partes = exchange.getRequestURI().getPath().split("/");
        boolean possuiId = partes.length > 2;

        cadastrar(exchange);
    }

    private void cadastrar(HttpExchange ex) throws IOException {
        Cliente cliente = JsonUtil.fromJson(lerBody(ex), Cliente.class);
        Cliente salvo = clienteService.cadastrar(cliente);
        responder(ex, 201, JsonUtil.toJson(salvo));
    }
}
