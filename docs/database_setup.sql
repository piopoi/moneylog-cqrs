DROP USER moneylog@'localhost';
DROP USER moneylog@'%';
FLUSH PRIVILEGES;

CREATE DATABASE moneylog DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER moneylog@'localhost' IDENTIFIED BY 'moneylog123#';
CREATE USER moneylog@'%' IDENTIFIED BY 'moneylog123#';

GRANT ALL PRIVILEGES ON moneylog.* TO 'moneylog'@'localhost';
GRANT ALL PRIVILEGES ON moneylog.* TO 'moneylog'@'%';
