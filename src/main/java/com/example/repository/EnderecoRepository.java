package com.example.repository;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import com.example.dto.EnderecoDTO;
import com.example.model.*;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class EnderecoRepository {
    private final Jdbi jdbi;

    public EnderecoRepository(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public void adicionarEndereco(EnderecoModel endereco, String documento) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("INSERT INTO endereco (documento, nome_rua, numero, complemento, bairro, cidade, estado, cep) VALUES (:documento, :nomeRua, :numero, :complemento, :bairro, :cidade, :estado, :cep)")
                .bind("documento", documento)
                .bindBean(endereco)
                .execute();
        });
    }

    public List<EnderecoDTO> listarEnderecos(String documento) {
        return jdbi.withHandle(handle -> 
            handle.createQuery("SELECT * FROM endereco WHERE documento = :documento")
                .bind("documento", documento)
                .mapToBean(EnderecoDTO.class)
                .list()
        );
    }

    public boolean deletarEndereco(String documento, String numero, String cep) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("DELETE FROM endereco WHERE documento = :documento AND numero = :numero AND cep = :cep")
                .bind("documento", documento)
                .bind("numero", numero)
                .bind("cep", cep)
                .execute();
        });
        return true;
    }
}
