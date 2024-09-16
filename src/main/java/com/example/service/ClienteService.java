package com.example.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.example.dto.ClienteDTO;
import com.example.model.ClienteModel;
import com.example.repository.ClienteRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

@ApplicationScoped
public class ClienteService {
    
    private final EnderecoService enderecoService;
    private final ClienteRepository clienteRepository;

    @Inject
    public ClienteService(ClienteRepository clienteRepository, EnderecoService enderecoService) {
        this.clienteRepository = clienteRepository;
        this.enderecoService = enderecoService;
    }

    public ClienteDTO cadastrarCliente(ClienteModel cliente) throws Exception {
        if (clienteRepository.validateDocumento(cliente.getDocumento())) { 
            throw new WebApplicationException("Documento já cadastrado.", Status.BAD_REQUEST);
        }
        
        ClienteDTO dto = new ClienteDTO();

        // cliente
        dto.setId(UUID.randomUUID());
        dto.setNome(cliente.getNome());

        validateCliente(cliente);
        dto.setTelefone("55" + cliente.getTelefone());

        if (cliente.getEmail().contains("@")) {
            dto.setEmail(cliente.getEmail().toLowerCase().trim());
        } else {
            throw new WebApplicationException("E-mail inválido.", Status.BAD_REQUEST);
        }

        dto.setDocumento(trataDocumento(cliente.getDocumento()));
        dto.setTipoDocumento(dto.getDocumento().length() == 11 ? "CPF" : "CNPJ");
        dto.setDataCriacao(LocalDateTime.now());

        // endereço
        dto.setEndereco(cliente.getEndereco());

        // salvar banco de dados
        clienteRepository.salvarCliente(dto);
        enderecoService.cadastrarEndereco(cliente.getEndereco(), cliente.getDocumento());
        
        return dto;
    }

    private void validateCliente(ClienteModel cliente) throws Exception {
        // Valida se o telefone contém apenas dígitos e tem entre 8 e 11 números
        if (!cliente.getTelefone().matches("\\d{8,11}")) {
            throw new WebApplicationException("Telefone deve conter apenas números e ter entre 8 e 11 dígitos.", Status.BAD_REQUEST);
        }
    }

    private String trataDocumento(String documento) throws Exception {
        documento = documento.replace(".", "").replace("-", "").replace("/", "").trim();
        if (documento.length() != 11 && documento.length() != 14) {
            throw new WebApplicationException("CPF ou CNPJ inválido", Status.BAD_REQUEST);
        }
        return documento;
    }
    
    public String editarCliente(String documento, ClienteModel cliente) {
        cliente.setDocumento(null);
        return clienteRepository.editarCliente(documento, cliente);
    }

    public List<ClienteDTO> listarClientes(String nome, String dataCriacao, int tamanhoPagina, int numeroPagina) {
        return clienteRepository.listarClientes(nome, dataCriacao, tamanhoPagina, numeroPagina);
    }
    
    public ClienteDTO buscarCliente(String documento) {
        return clienteRepository.buscarCliente(documento);
    }
}
