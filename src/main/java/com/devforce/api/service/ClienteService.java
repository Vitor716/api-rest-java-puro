package com.devforce.api.service;

import com.devforce.api.model.Cliente;
import com.devforce.api.repository.ClienteRepository;

public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public Cliente cadastrar(Cliente cliente) {
        validar(cliente);
        cliente.setId(null);
        return repository.salvar(cliente);
    }

    private void validar(Cliente cliente) {
        if (cliente == null || cliente.getNome() == null || cliente.getNome().isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
    }
}
