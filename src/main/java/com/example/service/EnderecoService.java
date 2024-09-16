package com.example.service;

import com.example.dto.*;
import com.example.model.*;
import com.example.repository.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

import java.util.List;

@ApplicationScoped
public class EnderecoService {

    private EnderecoRepository enderecoRepository;
    private ClienteRepository clienteRepository;

    public EnderecoService(EnderecoRepository enderecoRepository, ClienteRepository clienteRepository) {
        this.enderecoRepository = enderecoRepository;
        this.clienteRepository = clienteRepository;
    }

    public EnderecoModel cadastrarEndereco(EnderecoModel endereco, String documento) {
        try {
            if (!clienteRepository.validateDocumento(documento)){ 
                throw new WebApplicationException("Cliente não cadastrado.", Status.BAD_REQUEST);
            }
            validateEndereco(endereco);
            enderecoRepository.adicionarEndereco(endereco, documento);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return endereco;
    }    

    public boolean deletarEndereco(String documento, String numero, String cep) {
        return enderecoRepository.deletarEndereco(documento, numero, cep);
    }

    public List<EnderecoDTO> buscarEndereco(String documento) {
        return enderecoRepository.listarEnderecos(documento);
    }

    private void validateEndereco(EnderecoModel endereco) throws Exception {

        String regexLetras = "[\\p{L}\\s]+";
        String regexNumero = "\\d+";

        if (endereco.getNomeRua() == null || endereco.getNomeRua().trim().isEmpty()) {
            throw new WebApplicationException("Nome da rua não pode ser vazio.", Status.BAD_REQUEST);
        }
    
        if (endereco.getNumero() == null || endereco.getNumero().trim().isEmpty()) {
            throw new WebApplicationException("Número não pode ser vazio.", Status.BAD_REQUEST);
        }
        // Valida se o número contém apenas dígitos
        if (!endereco.getNumero().matches(regexNumero)) {
            throw new WebApplicationException("Número deve conter apenas dígitos.", Status.BAD_REQUEST);
        }

        if (endereco.getBairro() == null || endereco.getBairro().trim().isEmpty()) {
            throw new WebApplicationException("Bairro não pode ser vazio.", Status.BAD_REQUEST);
        }
    
        if (endereco.getCidade() == null || endereco.getCidade().trim().isEmpty()) {
            throw new WebApplicationException("Cidade não pode ser vazia.", Status.BAD_REQUEST);
        }
        // Valida se a cidade contém apenas letras e espaços
        if (!endereco.getCidade().matches(regexLetras)) {
            throw new WebApplicationException("Cidade deve conter apenas letras.", Status.BAD_REQUEST);
        }
    
        if (endereco.getEstado() == null || endereco.getEstado().trim().isEmpty()) {
            throw new WebApplicationException("Estado não pode ser vazio.", Status.BAD_REQUEST);
        }
        // Valida se o estado contém apenas letras e espaços
        if (!endereco.getEstado().matches(regexLetras)) {
            throw new WebApplicationException("Estado deve conter apenas letras.", Status.BAD_REQUEST);
        }
    
        if (endereco.getCep() == null || endereco.getCep().trim().isEmpty()) {
            throw new WebApplicationException("CEP não pode ser vazio.", Status.BAD_REQUEST);
        }
        if (!endereco.getCep().matches(regexNumero)) {
            throw new WebApplicationException("Formato de CEP inválido. Deve conter apenas números.", Status.BAD_REQUEST);
        }
        // Valida o formato do CEP (5 dígitos, traço, 3 dígitos)
        if (!endereco.getCep().matches("\\d{8,8}")) {
            throw new WebApplicationException("Formato de CEP inválido. Deve ser '12345678'.", Status.BAD_REQUEST);
        }
    }
}
