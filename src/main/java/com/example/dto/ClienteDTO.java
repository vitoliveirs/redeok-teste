package com.example.dto;

import lombok.Data;
import com.example.model.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
public class ClienteDTO {
    private UUID id;
    private String nome;
    private String telefone;
    private String email;
    private String documento;
    private String tipoDocumento;
    private LocalDateTime dataCriacao;
    private EnderecoModel endereco;
}
