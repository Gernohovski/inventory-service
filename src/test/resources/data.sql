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

INSERT INTO funcionalidade (funcionalidade) VALUES
    ('LOGIN'),
    ('REFRESH_TOKEN'),
    ('LOGOUT'),
    ('CADASTRO_USUARIO'),
    ('SOLICITAR_REDEFINICAO_SENHA'),
    ('ALTERAR_SENHA'),
    ('LISTAR_CATEGORIA'),
    ('CADASTRAR_CATEGORIA'),
    ('CADASTRAR_ITEM'),
    ('FILTRAR_ITENS'),
    ('BUSCAR_LOCALIZACAO'),
    ('CADASTRO_USUARIO_RELACIONADO'),
    ('REDEFINIR_SENHA'),
    ('DELETAR_CATEGORIA'),
    ('CADASTRO_LOCALIZACAO'),
    ('DELETAR_LOCALIZACAO'),
    ('LISTAR_LOCALIZACAO'),
    ('EDITAR_ITEM'),
    ('ALTERAR_STATUS_ITEM'),
    ('DELETAR_ITEM'),
    ('LISTAR_ITEM'),
    ('GERAR_RELATORIO_ITEM'),
    ('INICIAR_AUDITORIA'),
    ('GERAR_RELATORIO_AUDITORIA'),
    ('CONFIRMAR_LOCALIZACAO'),
    ('FINALIZAR_AUDITORIA');

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