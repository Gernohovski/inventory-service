INSERT INTO funcionalidade (funcionalidade, endpoint, http_method)
SELECT v FROM (VALUES
    ('LOGIN', '/auth-service/v1/autenticacao/login', 'POST'),
    ('REFRESH_TOKEN', '/auth-service/v1/autenticacao/refresh', 'POST'),
    ('LOGOUT', '/auth-service/v1/autenticacao/logout', 'POST'),
    ('CADASTRO_USUARIO', '/auth-service/v1/usuarios', 'POST'),
    ('SOLICITAR_REDEFINICAO_SENHA', '/auth-service/v1/usuarios/solicitar-redefinicao-senha', 'POST'),
    ('ALTERAR_SENHA', '/auth-service/v1/usuarios/alterar-senha', 'PUT'),
    ('LISTAR_CATEGORIA', '/core-service/v1/categorias', 'GET'),
    ('CADASTRAR_CATEGORIA', '/core-service/v1/categorias', 'POST'),
    ('CADASTRAR_ITEM', '/core-service/v1/itens', 'POST'),
    ('FILTRAR_ITENS', '/core-service/v1/itens', 'GET'),
    ('BUSCAR_LOCALIZACAO', '/core-service/v1/localizacao', 'GET'),
    ('CADASTRO_USUARIO_RELACIONADO', '/api/usuarios/relacionados', 'POST'),
    ('REDEFINIR_SENHA', '/auth-service/v1/usuarios/alterar-senha', 'PUT'),
    ('DELETAR_CATEGORIA', '/api/categorias/delete', 'DELETE'),
    ('CADASTRO_LOCALIZACAO', '/api/localizacoes/create', 'POST'),
    ('DELETAR_LOCALIZACAO', '/api/localizacoes/delete', 'DELETE'),
    ('LISTAR_LOCALIZACAO', '/api/localizacoes', 'GET'),
    ('EDITAR_ITEM', '/api/itens/edit', 'PUT'),
    ('ALTERAR_STATUS_ITEM', '/api/itens/status', 'PATCH'),
    ('DELETAR_ITEM', '/api/itens/delete', 'DELETE'),
    ('LISTAR_ITEM', '/api/itens/list', 'GET'),
    ('GERAR_RELATORIO_ITEM', '/api/relatorios/itens', 'GET'),
    ('INICIAR_AUDITORIA', '/api/auditorias/iniciar', 'POST'),
    ('GERAR_RELATORIO_AUDITORIA', '/api/relatorios/auditorias', 'GET'),
    ('CONFIRMAR_LOCALIZACAO', '/api/auditorias/confirmar-localizacao', 'PATCH'),
    ('FINALIZAR_AUDITORIA', '/api/auditorias/finalizar', 'PATCH');
) AS t(v)
WHERE NOT EXISTS (
  SELECT 1 FROM funcionalidade f WHERE f.funcionalidade = t.v
);

-- Inserir funções se não existirem
INSERT INTO funcao (nome)
SELECT v FROM (VALUES
  ('ADMIN'),
  ('OPERADOR')
) AS t(v)
WHERE NOT EXISTS (
  SELECT 1 FROM funcao f WHERE f.nome = t.v
);

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

INSERT INTO localizacao (andar, nome_sala)
SELECT
  'Térreo' as andar,
  'SALA' || LPAD(sala_num::text, 2, '0') as nome_sala
FROM generate_series(1, 10) as sala_num
WHERE NOT EXISTS (
  SELECT 1 FROM localizacao
  WHERE nome_sala = 'SALA' || LPAD(sala_num::text, 2, '0')
);

INSERT INTO localizacao (andar, nome_sala)
SELECT
  'Primeiro Andar' as andar,
  'LAB' || LPAD(lab_num::text, 2, '0') as nome_sala
FROM generate_series(1, 6) as lab_num
WHERE NOT EXISTS (
  SELECT 1 FROM localizacao
  WHERE nome_sala = 'LAB' || LPAD(lab_num::text, 2, '0')
);

INSERT INTO status_item (nome)
SELECT v FROM (VALUES
  ('Ativo'),
  ('Baixa'),
  ('Transferência'),
  ('Em manutenção'),
  ('Manutenção externa'),
  ('Outros')
) AS t(v)
WHERE NOT EXISTS (
  SELECT 1 FROM status_item s WHERE s.nome = t.v
);

INSERT INTO tipo_entrada (nome)
SELECT v FROM (VALUES
  ('Compra'),
  ('Doação'),
  ('Transferência')
) AS t(v)
WHERE NOT EXISTS (
  SELECT 1 FROM tipo_entrada t2 WHERE t2.nome = t.v
);