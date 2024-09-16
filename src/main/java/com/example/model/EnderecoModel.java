package com.example.model;

import lombok.Data;

@Data
public class EnderecoModel {
    String nomeRua;
    String numero;
    String complemento;
    String bairro;
    String cidade;
    String estado;
    String cep;
}
