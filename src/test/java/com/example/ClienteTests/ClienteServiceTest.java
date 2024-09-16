package com.example.ClienteTests;

import jakarta.ws.rs.WebApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.example.dto.ClienteDTO;
import com.example.model.ClienteModel;
import com.example.model.EnderecoModel;
import com.example.repository.ClienteRepository;
import com.example.service.ClienteService;
import com.example.service.EnderecoService;
import jakarta.ws.rs.core.Response.Status;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EnderecoService enderecoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCadastrarCliente_Success() throws Exception {
        ClienteModel cliente = new ClienteModel();
        cliente.setDocumento("12345678901");
        cliente.setNome("João");
        cliente.setTelefone("123456789");
        cliente.setEmail("joao@example.com");
        cliente.setEndereco(new EnderecoModel());

        // Configura o mock para o método validateDocumento
        when(clienteRepository.validateDocumento(cliente.getDocumento())).thenReturn(false);

        // Chama o método a ser testado
        ClienteDTO result = clienteService.cadastrarCliente(cliente);

        // Verificações
        assertNotNull(result, "O resultado não deve ser nulo");
        assertEquals("55" + cliente.getTelefone(), result.getTelefone(), "O telefone deve ser formatado corretamente");
        assertEquals(cliente.getEmail().toLowerCase().trim(), result.getEmail(), "O e-mail deve ser formatado corretamente");
        verify(clienteRepository).salvarCliente(result);
        verify(enderecoService).cadastrarEndereco(cliente.getEndereco(), cliente.getDocumento());
    }

    @Test
    public void testCadastrarCliente_DocumentoAlreadyExists() {
        ClienteModel cliente = new ClienteModel();
        cliente.setDocumento("50654202893");

        // Configura o mock para o método validateDocumento
        when(clienteRepository.validateDocumento(cliente.getDocumento())).thenReturn(true);

        // Chama o método e verifica a exceção
        WebApplicationException thrown = assertThrows(WebApplicationException.class, () -> {
            clienteService.cadastrarCliente(cliente);
        });

        // Verificações
        assertEquals("Documento já cadastrado.", thrown.getMessage());
    }
}
