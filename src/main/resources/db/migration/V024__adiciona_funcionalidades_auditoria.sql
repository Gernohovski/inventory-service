INSERT INTO funcionalidade (funcionalidade, endpoint, http_method) VALUES
    ('CRIAR_AUDITORIA', '/audit-service/v1/auditoria/iniciar', 'POST');

INSERT INTO funcionalidade (funcionalidade, endpoint, http_method) VALUES
    ('ENCERRAR_AUDITORIA', '/audit-service/v1/auditoria/encerrar', 'PUT');

INSERT INTO funcionalidade (funcionalidade, endpoint, http_method) VALUES
    ('AUDITAR_ITEM_AUDITORIA', '/audit-service/v1/auditoria/auditar-item', 'PUT');

INSERT INTO funcionalidade (funcionalidade, endpoint, http_method) VALUES
    ('CONSULTAR_ATIVA', '/audit-service/v1/auditoria/ativa', 'GET');

INSERT INTO funcionalidade (funcionalidade, endpoint, http_method) VALUES
    ('EDITAR_ITEM_AUDITORIA', '/audit-service/v1/auditoria/editar-item', 'PUT');

INSERT INTO funcionalidade (funcionalidade, endpoint, http_method) VALUES
    ('CONSULTAR_HISTORICO', '/audit-service/v1/auditoria/historico', 'GET');

INSERT INTO funcionalidade (funcionalidade, endpoint, http_method) VALUES
    ('BUSCAR_NO_HISTORICO', '/audit-service/v1/auditoria/historico/buscar', 'GET');

INSERT INTO funcao_funcionalidade (funcao_id, funcionalidade_id)
SELECT
  (SELECT id FROM funcao WHERE nome = 'ADMIN') as funcao_id,
  f.id as funcionalidade_id
FROM funcionalidade f
WHERE NOT EXISTS (
  SELECT 1
  FROM funcao_funcionalidade ff
  WHERE ff.funcao_id = (SELECT id FROM funcao WHERE nome = 'ADMIN')
  AND ff.funcionalidade_id = f.id
);

INSERT INTO funcao_funcionalidade (funcao_id, funcionalidade_id)
SELECT
    (SELECT id FROM funcao WHERE nome = 'OPERADOR') as funcao_id,
    (SELECT id FROM funcionalidade WHERE funcionalidade = 'AUDITAR_ITEM_AUDITORIA') as funcionalidade_id
WHERE NOT EXISTS (
    SELECT 1 FROM funcao_funcionalidade
    WHERE funcao_id = (SELECT id FROM funcao WHERE nome = 'OPERADOR')
    AND funcionalidade_id = (SELECT id FROM funcionalidade WHERE funcionalidade = 'AUDITAR_ITEM_AUDITORIA')
);

INSERT INTO funcao_funcionalidade (funcao_id, funcionalidade_id)
SELECT
    (SELECT id FROM funcao WHERE nome = 'OPERADOR') as funcao_id,
    (SELECT id FROM funcionalidade WHERE funcionalidade = 'EDITAR_ITEM_AUDITORIA') as funcionalidade_id
WHERE NOT EXISTS (
    SELECT 1 FROM funcao_funcionalidade
    WHERE funcao_id = (SELECT id FROM funcao WHERE nome = 'OPERADOR')
    AND funcionalidade_id = (SELECT id FROM funcionalidade WHERE funcionalidade = 'EDITAR_ITEM_AUDITORIA')
);

INSERT INTO funcao_funcionalidade (funcao_id, funcionalidade_id)
SELECT
    (SELECT id FROM funcao WHERE nome = 'OPERADOR') as funcao_id,
    (SELECT id FROM funcionalidade WHERE funcionalidade = 'CONSULTAR_ATIVA') as funcionalidade_id
WHERE NOT EXISTS (
    SELECT 1 FROM funcao_funcionalidade
    WHERE funcao_id = (SELECT id FROM funcao WHERE nome = 'OPERADOR')
    AND funcionalidade_id = (SELECT id FROM funcionalidade WHERE funcionalidade = 'CONSULTAR_ATIVA')
);

INSERT INTO funcao_funcionalidade (funcao_id, funcionalidade_id)
SELECT
    (SELECT id FROM funcao WHERE nome = 'OPERADOR') as funcao_id,
    (SELECT id FROM funcionalidade WHERE funcionalidade = 'FILTRAR_ITENS') as funcionalidade_id
WHERE NOT EXISTS (
    SELECT 1 FROM funcao_funcionalidade
    WHERE funcao_id = (SELECT id FROM funcao WHERE nome = 'OPERADOR')
    AND funcionalidade_id = (SELECT id FROM funcionalidade WHERE funcionalidade = 'FILTRAR_ITENS')
);