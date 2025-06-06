CREATE DATABASE IF NOT EXISTS `stock_db`;
CREATE USER 'stock_user'@'%' IDENTIFIED BY 'password';
GRANT ALL ON `stock_db`.* TO 'stock_user'@'%';
FLUSH PRIVILEGES;