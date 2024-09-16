CREATE TABLE cliente (
    id UUID PRIMARY KEY,
    nome VARCHAR(255),
    telefone VARCHAR(20),
    email VARCHAR(255),
    documento VARCHAR(20),
    tipo_documento VARCHAR(10),
    data_criacao TIMESTAMP,
    nome_rua VARCHAR(255),
    numero VARCHAR(100),
    complemento VARCHAR(255),
    bairro VARCHAR(255),
    cidade VARCHAR(255),
    estado VARCHAR(255),
    cep VARCHAR(100)
);
