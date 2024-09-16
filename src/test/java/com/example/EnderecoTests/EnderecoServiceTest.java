package com.example.service;

import com.example.dto.EnderecoDTO;
import com.example.model.EnderecoModel;
import com.example.service.*;
import com.example.repository.*;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EnderecoServiceTest {

    @InjectMocks
    private EnderecoService enderecoService;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCadastrarEndereco_Success() {
        EnderecoModel endereco = new EnderecoModel();
        endereco.setNomeRua("Rua Teste");
        endereco.setNumero("123");
        endereco.setBairro("Bairro Teste");
        endereco.setCidade("Cidade Teste");
        endereco.setEstado("Estado Teste");
        endereco.setCep("12345678");

        String documento = "12345678901";

        // Configura o mock para o método validateDocumento
        when(clienteRepository.validateDocumento(documento)).thenReturn(true);

        // Configura o mock para adicionar o endereço
        doNothing().when(enderecoRepository).adicionarEndereco(endereco, documento);

        // Chama o método a ser testado
        EnderecoModel result = enderecoService.cadastrarEndereco(endereco, documento);

        // Verificações
        assertNotNull(result, "O resultado não deve ser nulo");
        assertEquals(endereco, result, "O endereço retornado deve ser o mesmo que o endereço de entrada");
        verify(enderecoRepository).adicionarEndereco(endereco, documento);
    }

    @Test
    public void testDeletarEndereco_Success() {
        String documento = "12345678901";
        String numero = "123";
        String cep = "12345678";

        // Configura o mock para o método deletarEndereco
        when(enderecoRepository.deletarEndereco(documento, numero, cep)).thenReturn(true);

        // Chama o método a ser testado
        boolean result = enderecoService.deletarEndereco(documento, numero, cep);

        // Verificações
        assertTrue(result, "O endereço deveria ser deletado com sucesso");
        verify(enderecoRepository).deletarEndereco(documento, numero, cep);
    }

    @Test
    public void testBuscarEndereco_Success() {
        String documento = "12345678901";
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        List<EnderecoDTO> enderecoList = Arrays.asList(enderecoDTO);

        // Configura o mock para listarEnderecos
        when(enderecoRepository.listarEnderecos(documento)).thenReturn(enderecoList);

        // Chama o método a ser testado
        List<EnderecoDTO> result = enderecoService.buscarEndereco(documento);

        // Verificações
        assertNotNull(result, "A lista de endereços não deve ser nula");
        assertEquals(enderecoList, result, "A lista de endereços retornada deve ser a mesma que a lista de entrada");
        verify(enderecoRepository).listarEnderecos(documento);
    }
}
