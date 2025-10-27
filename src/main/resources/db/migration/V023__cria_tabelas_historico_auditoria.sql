CREATE TABLE auditoria (
    id BIGSERIAL PRIMARY KEY,
    codigo_auditoria VARCHAR(20) UNIQUE NOT NULL,
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP NOT NULL,
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_auditoria_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuario(id)
);

CREATE TABLE item_auditado (
    id BIGSERIAL PRIMARY KEY,
    auditoria_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    observacao TEXT,
    data_verificacao TIMESTAMP,
    conformidade BOOLEAN,
    CONSTRAINT fk_item_auditado_auditoria FOREIGN KEY (auditoria_id)
        REFERENCES auditoria(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_auditado_item FOREIGN KEY (item_id)
        REFERENCES item(id)
);

CREATE TABLE auditoria_historico (
    id BIGSERIAL PRIMARY KEY,
    codigo_auditoria VARCHAR(20) UNIQUE NOT NULL,
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP NOT NULL,
    usuario_responsavel_id BIGINT NOT NULL,
    usuario_responsavel_nome VARCHAR(255) NOT NULL,
    total_itens BIGINT NOT NULL
);

CREATE TABLE item_auditado_historico (
    id BIGSERIAL PRIMARY KEY,
    auditoria_historico_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    item_codigo VARCHAR(100) NOT NULL,
    item_nome VARCHAR(255) NOT NULL,
    item_categoria VARCHAR(255),
    item_localizacao VARCHAR(255),
    item_status VARCHAR(100),
    item_numero_serie VARCHAR(100),
    observacao TEXT,
    data_verificacao TIMESTAMP,
    conformidade BOOLEAN NOT NULL,
    CONSTRAINT fk_item_auditado_historico_auditoria FOREIGN KEY (auditoria_historico_id)
        REFERENCES auditoria_historico(id) ON DELETE CASCADE
);