package com.example.model;

import lombok.Data;

@Data
public class ClienteModel {
    String nome;
    String telefone;
    String email;
    String documento;
    EnderecoModel endereco;
}
