INSERT INTO funcionalidade (funcionalidade, endpoint, http_method) VALUES
    ('DESATIVAR_AUDITORIA_TODOS', '/auth-service/v1/usuarios/destivar-autoria/todos', 'PUT');

INSERT INTO funcionalidade (funcionalidade, endpoint, http_method) VALUES
    ('ATIVAR_AUDITORIA_TODOS', '/auth-service/v1/usuarios/ativar-autoria/todos', 'PUT');

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