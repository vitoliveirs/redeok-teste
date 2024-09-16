CREATE TABLE endereco (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    documento VARCHAR(14) NOT NULL,
    nome_rua VARCHAR(255),
    numero VARCHAR(10),
    complemento VARCHAR(255),
    bairro VARCHAR(255),
    cidade VARCHAR(255),
    estado VARCHAR(255),
    cep VARCHAR(10)
);
