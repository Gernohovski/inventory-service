INSERT INTO funcionalidade (funcionalidade, endpoint, http_method) VALUES
    ('EXPORTAR_ITEMS', '/core-service/v1/itens/exportar/pdf', 'POST');

INSERT INTO funcionalidade (funcionalidade, endpoint, http_method) VALUES
    ('EXPORTAR_ITEMS', '/core-service/v1/itens/exportar/csv', 'POST');

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
