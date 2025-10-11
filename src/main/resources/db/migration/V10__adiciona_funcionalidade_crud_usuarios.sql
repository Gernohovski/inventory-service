INSERT INTO funcionalidade (funcionalidade, endpoint, http_method) VALUES
    ('CONSULTAR_USUARIOS', '/auth-service/v1/usuarios', 'GET');

INSERT INTO funcionalidade (funcionalidade, endpoint, http_method) VALUES
    ('CONSULTAR_ADMINISTRADORES', '/auth-service/v1/usuarios/administradores', 'GET');

INSERT INTO funcionalidade (funcionalidade, endpoint, http_method) VALUES
    ('DELETAR_USUARIOS', '/auth-service/v1/usuarios', 'DELETE');

INSERT INTO funcionalidade (funcionalidade, endpoint, http_method) VALUES
    ('ATUALIZAR_USUARIOS', '/auth-service/v1/usuarios', 'PUT');

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
