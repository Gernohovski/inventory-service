CREATE TABLE IF NOT EXISTS categoria_item (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS localizacao (
    id BIGSERIAL PRIMARY KEY,
    andar VARCHAR(50) NOT NULL,
    nome_sala VARCHAR(100) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS status_item (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS tipo_entrada (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS item (
    id BIGSERIAL PRIMARY KEY,
    nome_item VARCHAR(200) NOT NULL,
    descricao_curta TEXT,
    descricao_detalhada TEXT,
    numero_serie VARCHAR(100),
    codigo_item VARCHAR(100) UNIQUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_alteracao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    categoria_item_id BIGINT REFERENCES categoria_item(id),
    localizacao_id BIGINT REFERENCES localizacao(id),
    status_item_id BIGINT REFERENCES status_item(id),
    tipo_entrada_id BIGINT REFERENCES tipo_entrada(id)
);
