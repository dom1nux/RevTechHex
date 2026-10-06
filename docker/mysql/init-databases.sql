-- Initial schema and user provisioning for RevTech Microservices (MySQL 8.4 LTS)

-- 1. Create databases per bounded context
CREATE DATABASE IF NOT EXISTS `administrativa` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `citas`          CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `clientes`       CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `identidad`      CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `inspection`     CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS `pagos`          CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 2. Create dedicated users per microservice
CREATE USER IF NOT EXISTS 'administrativa_user'@'%' IDENTIFIED BY 'administrativa_pass';
CREATE USER IF NOT EXISTS 'citas_user'@'%'          IDENTIFIED BY 'citas_pass';
CREATE USER IF NOT EXISTS 'clientes_user'@'%'       IDENTIFIED BY 'clientes_pass';
CREATE USER IF NOT EXISTS 'identidad_user'@'%'      IDENTIFIED BY 'identidad_pass';
CREATE USER IF NOT EXISTS 'inspection_user'@'%'     IDENTIFIED BY 'inspection_pass';
CREATE USER IF NOT EXISTS 'pagos_user'@'%'          IDENTIFIED BY 'pagos_pass';

-- 3. Grant isolated schema privileges
GRANT ALL PRIVILEGES ON `administrativa`.* TO 'administrativa_user'@'%';
GRANT ALL PRIVILEGES ON `citas`.*          TO 'citas_user'@'%';
GRANT ALL PRIVILEGES ON `clientes`.*       TO 'clientes_user'@'%';
GRANT ALL PRIVILEGES ON `identidad`.*      TO 'identidad_user'@'%';
GRANT ALL PRIVILEGES ON `inspection`.*     TO 'inspection_user'@'%';
GRANT ALL PRIVILEGES ON `pagos`.*          TO 'pagos_user'@'%';

FLUSH PRIVILEGES;
