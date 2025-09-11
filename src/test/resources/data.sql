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
    ('ESTOQUISTA'),
    ('AUDITOR');

INSERT INTO funcionalidade (funcionalidade) VALUES
    ('/excluir-bem'),
    ('/cadastrar-bem'),
    ('/listar-bem'),
    ('/editar-bem');

INSERT INTO funcao_funcionalidade (funcao_id, funcionalidade_id) VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4);

INSERT INTO funcao_funcionalidade (funcao_id, funcionalidade_id) VALUES
    (2, 2),
    (2, 3);

INSERT INTO funcao_funcionalidade (funcao_id, funcionalidade_id) VALUES
    (3, 3);

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