CREATE DATABASE IF NOT EXISTS `keycloak`;
CREATE USER 'keycloak_user'@'%' IDENTIFIED BY 'password';
GRANT ALL ON `keycloak`.* TO 'keycloak_user'@'%';
FLUSH PRIVILEGES;