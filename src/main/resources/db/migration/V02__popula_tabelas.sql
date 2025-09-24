INSERT INTO funcionalidade (funcionalidade, endpoint, http_method) VALUES
    ('LOGIN', '/auth-service/v1/autenticacao/login', 'POST'),
    ('REFRESH_TOKEN', '/auth-service/v1/autenticacao/refresh', 'POST'),
    ('LOGOUT', '/auth-service/v1/autenticacao/logout', 'POST'),
    ('CADASTRO_USUARIO', '/auth-service/v1/usuarios', 'POST'),
    ('SOLICITAR_REDEFINICAO_SENHA', '/auth-service/v1/usuarios/solicitar-redefinicao-senha', 'POST'),
    ('ALTERAR_SENHA', '/auth-service/v1/usuarios/alterar-senha', 'PUT'),
    ('LISTAR_CATEGORIA', '/core-service/v1/categorias', 'GET'),
    ('LISTAR_CATEGORIA_PAGINADO', '/core-service/v1/categorias/paginado', 'GET'),
    ('DELETAR_CATEGORIA', '/core-service/v1/categorias', 'DELETE'),
    ('ATUALIZAR_CATEGORIA', '/core-service/v1/categorias', 'PUT'),
    ('CADASTRAR_CATEGORIA', '/core-service/v1/categorias', 'POST'),
    ('CADASTRAR_ITEM', '/core-service/v1/itens', 'POST'),
    ('FILTRAR_ITENS', '/core-service/v1/itens', 'GET'),
    ('BUSCAR_LOCALIZACAO', '/core-service/v1/localizacao', 'GET'),
    ('CADASTRO_USUARIO_RELACIONADO', '/api/usuarios/relacionados', 'POST'),
    ('REDEFINIR_SENHA', '/auth-service/v1/usuarios/alterar-senha', 'PUT');

INSERT INTO funcao (nome) VALUES
  ('ADMIN'),
  ('OPERADOR');

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
  f.id as funcionalidade_id
FROM funcionalidade f
WHERE f.funcionalidade IN (
  'CONFIRMAR_LOCALIZACAO',
  'FINALIZAR_AUDITORIA'
)
AND NOT EXISTS (
  SELECT 1
  FROM funcao_funcionalidade ff
  WHERE ff.funcao_id = (SELECT id FROM funcao WHERE nome = 'OPERADOR')
  AND ff.funcionalidade_id = f.id
);

INSERT INTO status_item (nome) VALUES
  ('Ativo'),
  ('Baixa'),
  ('Transferência'),
  ('Em manutenção'),
  ('Manutenção externa'),
  ('Outros');

INSERT INTO tipo_entrada (nome) VALUES
  ('Compra'),
  ('Doação'),
  ('Transferência');