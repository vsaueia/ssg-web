CREATE OR REPLACE VIEW jaas_jdbc AS 
 SELECT u.login, u.senha, p.nome AS permissao
   FROM usuario u
   JOIN usuario_permissao up ON up.usuario_id = u.id
   JOIN permissao p ON up.permissao_id = p.id;