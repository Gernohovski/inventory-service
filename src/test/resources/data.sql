DELETE FROM usuario_funcao;
DELETE FROM funcao_funcionalidade;
DELETE FROM funcionalidade;
DELETE FROM funcao;
DELETE FROM categoria_item;
DELETE FROM localizacao;
DELETE FROM item;
DELETE FROM status_item;
DELETE FROM tipo_entrada;

ALTER TABLE funcao ALTER COLUMN id RESTART WITH 1;
ALTER TABLE funcionalidade ALTER COLUMN id RESTART WITH 1;
ALTER TABLE categoria_item ALTER COLUMN id RESTART WITH 1;
ALTER TABLE localizacao ALTER COLUMN id RESTART WITH 1;
ALTER TABLE status_item ALTER COLUMN id RESTART WITH 1;
ALTER TABLE tipo_entrada ALTER COLUMN id RESTART WITH 1;
ALTER TABLE item ALTER COLUMN id RESTART WITH 1;

INSERT INTO funcao (nome) VALUES
    ('ADMIN'),
    ('OPERADOR');

INSERT INTO funcionalidade (funcionalidade, endpoint, http_method) VALUES
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

INSERT INTO funcao_funcionalidade (funcao_id, funcionalidade_id) VALUES
    -- ADMIN tem acesso a todas as funcionalidades
    (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10),
    (1, 11), (1, 12), (1, 13), (1, 14), (1, 15), (1, 16), (1, 17), (1, 18), (1, 19), (1, 20),
    (1, 21), (1, 22), (1, 23), (1, 24), (1, 25), (1, 26);

INSERT INTO funcao_funcionalidade (funcao_id, funcionalidade_id) VALUES
    (2, 25), (2, 26);

INSERT INTO categoria_item (nome) VALUES
    ('CADEIRA'),
    ('MESA'),
    ('COMPUTADOR');

INSERT INTO localizacao (andar, nome_sala) VALUES
    ('1', 'LAB01'),
    ('1', 'LAB02'),
    ('1', 'LAB03');

INSERT INTO status_item (nome) VALUES
    ('NOVO'),
    ('USADO');

INSERT INTO tipo_entrada (nome) VALUES
    ('COMPRA'),
    ('DOACAO');