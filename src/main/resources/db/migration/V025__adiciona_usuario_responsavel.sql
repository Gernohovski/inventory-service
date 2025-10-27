ALTER TABLE item_auditado ADD usuario_id BIGINT;

ALTER TABLE item_auditado ADD CONSTRAINT fk_item_auditado_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuario(id);

ALTER TABLE item_auditado_historico ADD usuario_nome VARCHAR(100);