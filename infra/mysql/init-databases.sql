-- Initial schema and user provisioning for the RevTech monolith (MySQL 8.4 LTS)
-- A single schema: each bounded context owns its own tables inside it.

CREATE DATABASE IF NOT EXISTS `revtech` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'revtech_user'@'%' IDENTIFIED BY 'revtech_pass';

GRANT ALL PRIVILEGES ON `revtech`.* TO 'revtech_user'@'%';

FLUSH PRIVILEGES;
