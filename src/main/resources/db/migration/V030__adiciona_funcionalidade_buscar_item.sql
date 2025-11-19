INSERT INTO funcionalidade (funcionalidade, endpoint, http_method) VALUES
    ('BUSCAR_ITEM_PELO_CODIGO', '/core-service/v1/itens/buscar', 'GET');

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
    (SELECT id FROM funcionalidade WHERE funcionalidade = 'BUSCAR_ITEM_PELO_CODIGO') as funcionalidade_id
WHERE NOT EXISTS (
    SELECT 1 FROM funcao_funcionalidade
    WHERE funcao_id = (SELECT id FROM funcao WHERE nome = 'OPERADOR')
    AND funcionalidade_id = (SELECT id FROM funcionalidade WHERE funcionalidade = 'BUSCAR_ITEM_PELO_CODIGO')
);