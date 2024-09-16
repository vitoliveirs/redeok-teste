package com.example.repository;

import com.example.dto.*;
import com.example.model.ClienteModel;
import com.example.model.EnderecoModel;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.jdbi.v3.core.Jdbi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ClienteRepository {

    @Inject
    Jdbi jdbi;

    public void salvarCliente(ClienteDTO cliente) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("INSERT INTO cliente (id, nome, telefone, email, documento, tipo_documento, data_criacao, nome_rua, numero, complemento, bairro, cidade, estado, cep) " +
                    "VALUES (:id, :nome, :telefone, :email, :documento, :tipoDocumento, :dataCriacao, :nomeRua, :numero, :complemento, :bairro, :cidade, :estado, :cep)")
                    .bind("id", cliente.getId())
                    .bind("nome", cliente.getNome())
                    .bind("telefone", cliente.getTelefone())
                    .bind("email", cliente.getEmail())
                    .bind("documento", cliente.getDocumento())
                    .bind("tipoDocumento", cliente.getTipoDocumento())
                    .bind("dataCriacao", cliente.getDataCriacao())
                    .bind("nomeRua", cliente.getEndereco().getNomeRua())
                    .bind("numero", cliente.getEndereco().getNumero())
                    .bind("complemento", cliente.getEndereco().getComplemento())
                    .bind("bairro", cliente.getEndereco().getBairro())
                    .bind("cidade", cliente.getEndereco().getCidade())
                    .bind("estado", cliente.getEndereco().getEstado())
                    .bind("cep", cliente.getEndereco().getCep())
                    .execute();
        });
    }

    public ClienteDTO buscarCliente(String documento) {
        return jdbi.withHandle(handle -> 
            handle.createQuery("SELECT id, nome, telefone, email, documento, tipo_documento, data_criacao, nome_rua, numero, complemento, bairro, cidade, estado, cep FROM cliente WHERE documento = :documento")
                .bind("documento", documento)
                .map((rs, ctx) -> {
                    ClienteDTO cliente = new ClienteDTO();
                    cliente.setId(UUID.fromString(rs.getString("id")));
                    cliente.setNome(rs.getString("nome"));
                    cliente.setTelefone(rs.getString("telefone"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setDocumento(rs.getString("documento"));
                    cliente.setTipoDocumento(rs.getString("tipo_documento"));
                    cliente.setDataCriacao(rs.getObject("data_criacao", LocalDateTime.class));

                    EnderecoModel endereco = new EnderecoModel();
                    endereco.setNomeRua(rs.getString("nome_rua"));
                    endereco.setNumero(rs.getString("numero"));
                    endereco.setComplemento(rs.getString("complemento"));
                    endereco.setBairro(rs.getString("bairro"));
                    endereco.setCidade(rs.getString("cidade"));
                    endereco.setEstado(rs.getString("estado"));
                    endereco.setCep(rs.getString("cep"));

                    cliente.setEndereco(endereco);
                    return cliente;
                })
                .findFirst() 
                .orElse(null)
        );
    }

    public List<ClienteDTO> listarClientes(String nome, String dataCriacao, int tamanhoPagina, int numeroPagina) {
        int offset = numeroPagina * tamanhoPagina;
        return jdbi.withHandle(handle ->
            handle.createQuery("SELECT id, nome, telefone, email, documento, tipo_documento, data_criacao, nome_rua, numero, complemento, bairro, cidade, estado, cep FROM cliente " +
                    "WHERE (:nome IS NULL OR LOWER(nome) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
                    "AND (:dataCriacao IS NULL OR TO_CHAR(data_criacao, 'DD/MM/YYYY') = :dataCriacao) " +
                    "ORDER BY data_criacao ASC " +
                    "LIMIT :limit OFFSET :offset")
                .bind("nome", nome)
                .bind("dataCriacao", dataCriacao)
                .bind("limit", tamanhoPagina)
                .bind("offset", offset)
                .map((rs, ctx) -> {
                    ClienteDTO cliente = new ClienteDTO();
                    cliente.setId(UUID.fromString(rs.getString("id")));
                    cliente.setNome(rs.getString("nome"));
                    cliente.setTelefone(rs.getString("telefone"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setDocumento(rs.getString("documento"));
                    cliente.setTipoDocumento(rs.getString("tipo_documento"));
                    cliente.setDataCriacao(rs.getObject("data_criacao", LocalDateTime.class));

                    EnderecoModel endereco = new EnderecoModel();
                    endereco.setNomeRua(rs.getString("nome_rua"));
                    endereco.setNumero(rs.getString("numero"));
                    endereco.setComplemento(rs.getString("complemento"));
                    endereco.setBairro(rs.getString("bairro"));
                    endereco.setCidade(rs.getString("cidade"));
                    endereco.setEstado(rs.getString("estado"));
                    endereco.setCep(rs.getString("cep"));

                    cliente.setEndereco(endereco);
                    return cliente;
                })
                .list()
        );
    }

    public String editarCliente(String documento, ClienteModel cliente) {
        jdbi.useHandle(handle -> {
            StringBuilder sql = new StringBuilder("UPDATE cliente SET ");
            
            if (cliente.getNome() != null) sql.append("nome = COALESCE(:nome, nome), ");
            if (cliente.getTelefone() != null) sql.append("telefone = COALESCE(:telefone, telefone), ");
            if (cliente.getEmail() != null) sql.append("email = COALESCE(:email, email), ");
            if (cliente.getEndereco() != null) {
                sql.append("nome_rua = COALESCE(:nomeRua, nome_rua), ")
                   .append("numero = COALESCE(:numero, numero), ")
                   .append("complemento = COALESCE(:complemento, complemento), ")
                   .append("bairro = COALESCE(:bairro, bairro), ")
                   .append("cidade = COALESCE(:cidade, cidade), ")
                   .append("estado = COALESCE(:estado, estado), ")
                   .append("cep = COALESCE(:cep, cep), ");
            }
            
            sql.setLength(sql.length() - 2);
            sql.append(" WHERE documento = :documento");
    
            handle.createUpdate(sql.toString())
                .bind("documento", documento)
                .bind("nome", cliente.getNome())
                .bind("telefone", cliente.getTelefone())
                .bind("email", cliente.getEmail())
                .bind("nomeRua", cliente.getEndereco() != null ? cliente.getEndereco().getNomeRua() : null)
                .bind("numero", cliente.getEndereco() != null ? cliente.getEndereco().getNumero() : null)
                .bind("complemento", cliente.getEndereco() != null ? cliente.getEndereco().getComplemento() : null)
                .bind("bairro", cliente.getEndereco() != null ? cliente.getEndereco().getBairro() : null)
                .bind("cidade", cliente.getEndereco() != null ? cliente.getEndereco().getCidade() : null)
                .bind("estado", cliente.getEndereco() != null ? cliente.getEndereco().getEstado() : null)
                .bind("cep", cliente.getEndereco() != null ? cliente.getEndereco().getCep() : null)
                .execute();
        });
        return "Informações atualizadas com sucesso!";
    }
    
    
    public boolean validateDocumento(String documento) {
        return jdbi.withHandle(handle -> 
            handle.createQuery("SELECT 1 FROM cliente WHERE documento = :documento")
                  .bind("documento", documento)
                  .mapTo(Integer.class)
                  .findFirst()           
                  .isPresent()           
        );
    }
}
