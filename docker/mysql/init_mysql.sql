# DROP USER moneylog_cqrs@'localhost';
# DROP USER moneylog_cqrs@'%';
# FLUSH PRIVILEGES;

CREATE DATABASE moneylog_cqrs DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER moneylog_cqrs@'localhost' IDENTIFIED BY 'password';
CREATE USER moneylog_cqrs@'%' IDENTIFIED BY 'password';

GRANT ALL PRIVILEGES ON moneylog_cqrs.* TO 'moneylog_cqrs'@'localhost';
GRANT ALL PRIVILEGES ON moneylog_cqrs.* TO 'moneylog_cqrs'@'%';
