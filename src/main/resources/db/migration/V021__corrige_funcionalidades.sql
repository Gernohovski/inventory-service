UPDATE funcionalidade 
SET endpoint = '/auth-service/v1/usuarios/desativar-auditoria/todos', 
    http_method = 'PUT'
WHERE funcionalidade = 'DESATIVAR_AUDITORIA_TODOS';

UPDATE funcionalidade 
SET endpoint = '/auth-service/v1/usuarios/ativar-auditoria/todos', 
    http_method = 'PUT'
WHERE funcionalidade = 'ATIVAR_AUDITORIA_TODOS';

UPDATE funcionalidade 
SET funcionalidade = 'EXPORTAR_TODOS_PDF',
    endpoint = '/core-service/v1/itens/exportar/todos/pdf', 
    http_method = 'GET'
WHERE funcionalidade = 'EXPORTAR_TODOS';

INSERT INTO funcionalidade (funcionalidade, endpoint, http_method) VALUES
    ('EXPORTAR_TODOS_CSV', '/core-service/v1/itens/exportar/todos/csv', 'GET');
