INSERT INTO funcionalidade (funcionalidade)
SELECT v FROM (VALUES
  ('CADASTRO_USUARIO_RELACIONADO'),
  ('REDEFINIR_SENHA'),
  ('CADASTRO_CATEGORIA'),
  ('DELETAR_CATEGORIA'),
  ('LISTAR_CATEGORIA'),
  ('CADASTRO_LOCALIZACAO'),
  ('DELETAR_LOCALIZACAO'),
  ('LISTAR_LOCALIZCAO'),
  ('CADASTRO_ITEM'),
  ('EDITAR_ITEM'),
  ('ALTERAR_STATUS_ITEM'),
  ('DELETAR_ITEM'),
  ('LISTAR_ITEM'),
  ('GERAR_RELATORIO_ITEM'),
  ('INICIAR_AUDITORIA'),
  ('GERAR_RELATORIO_AUDITORIA'),
  ('CONFIRMAR_LOCALIZACAO'),
  ('FINALIZAR_AUDITORIA')
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