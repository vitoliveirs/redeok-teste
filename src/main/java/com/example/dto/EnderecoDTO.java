package com.example.dto;

import lombok.Data;

@Data
public class EnderecoDTO {
    String documento;
    String nomeRua;
    String numero;
    String complemento;
    String bairro;
    String cidade;
    String estado;
    String cep;
}
