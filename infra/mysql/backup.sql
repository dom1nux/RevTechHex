
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP TABLE IF EXISTS `actas_observaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actas_observaciones` (
  `id_acta` bigint NOT NULL AUTO_INCREMENT,
  `fecha_emision` datetime(6) NOT NULL,
  `inspeccion_id` bigint DEFAULT NULL,
  `observaciones` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id_acta`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `actas_observaciones` WRITE;
/*!40000 ALTER TABLE `actas_observaciones` DISABLE KEYS */;
INSERT INTO `actas_observaciones` VALUES (1,'2026-10-06 23:08:55.746520',4,'Frenos con eficiencia por debajo del minimo; reparar y reinspeccionar.'),(2,'2026-10-06 23:08:56.265301',6,'Faros desalineados y neumaticos con desgaste irregular.'),(3,'2026-10-06 23:08:56.578601',8,'Emisiones sobre el limite permitido.');
/*!40000 ALTER TABLE `actas_observaciones` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `certificados_inspeccion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `certificados_inspeccion` (
  `id_certificado` bigint NOT NULL AUTO_INCREMENT,
  `fecha_emision` datetime(6) NOT NULL,
  `inspeccion_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id_certificado`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `certificados_inspeccion` WRITE;
/*!40000 ALTER TABLE `certificados_inspeccion` DISABLE KEYS */;
INSERT INTO `certificados_inspeccion` VALUES (1,'2026-10-06 23:08:54.943181',1),(2,'2026-10-06 23:08:55.220963',2),(3,'2026-10-06 23:08:55.483565',3),(4,'2026-10-06 23:08:56.017023',5);
/*!40000 ALTER TABLE `certificados_inspeccion` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `clientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `clientes` (
  `id_cliente` bigint NOT NULL AUTO_INCREMENT,
  `doc_ident` varchar(20) NOT NULL,
  `fecha_registro` datetime(6) NOT NULL,
  `nombre` varchar(150) NOT NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE KEY `uk_cliente_doc_ident` (`doc_ident`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `clientes` WRITE;
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
INSERT INTO `clientes` VALUES (1,'45871236','2026-10-06 23:08:54.189466','Carlos Mendoza Rojas'),(2,'40125987','2026-10-06 23:08:54.224394','Ana Torres Quispe'),(3,'72458963','2026-10-06 23:08:54.246303','Luis Fernandez Soto'),(4,'08147529','2026-10-06 23:08:54.266984','Maria Gutierrez Paredes'),(5,'46321587','2026-10-06 23:08:54.286642','Jose Huaman Ccori'),(6,'70236914','2026-10-06 23:08:54.307647','Patricia Salazar Vega'),(7,'10458723','2026-10-06 23:08:54.330990','Transportes Andinos SAC'),(8,'44785210','2026-10-06 23:08:54.353318','Diego Ramirez Luna');
/*!40000 ALTER TABLE `clientes` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `inspeccion_pruebas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inspeccion_pruebas` (
  `inspeccion_id` bigint NOT NULL,
  `prueba` varchar(100) NOT NULL,
  `resultado` varchar(100) NOT NULL,
  KEY `FKbeektd17myq0s7uq400ifiep4` (`inspeccion_id`),
  CONSTRAINT `FKbeektd17myq0s7uq400ifiep4` FOREIGN KEY (`inspeccion_id`) REFERENCES `inspecciones_tecnicas` (`id_inspeccion`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `inspeccion_pruebas` WRITE;
/*!40000 ALTER TABLE `inspeccion_pruebas` DISABLE KEYS */;
INSERT INTO `inspeccion_pruebas` VALUES (1,'FRENOS','APROBADO'),(1,'EMISIONES','APROBADO'),(1,'SUSPENSION','APROBADO'),(1,'DIRECCION','APROBADO'),(1,'LUCES','APROBADO'),(1,'NEUMATICOS','APROBADO'),(2,'FRENOS','APROBADO'),(2,'EMISIONES','APROBADO'),(2,'SUSPENSION','APROBADO'),(2,'DIRECCION','APROBADO'),(2,'LUCES','APROBADO'),(2,'NEUMATICOS','APROBADO'),(3,'FRENOS','APROBADO'),(3,'EMISIONES','APROBADO'),(3,'SUSPENSION','APROBADO'),(3,'DIRECCION','APROBADO'),(3,'LUCES','APROBADO'),(3,'NEUMATICOS','APROBADO'),(4,'FRENOS','OBSERVADO'),(4,'NEUMATICOS','APROBADO'),(4,'DIRECCION','APROBADO'),(4,'SUSPENSION','APROBADO'),(4,'LUCES','APROBADO'),(4,'EMISIONES','APROBADO'),(5,'FRENOS','APROBADO'),(5,'EMISIONES','APROBADO'),(5,'SUSPENSION','APROBADO'),(5,'DIRECCION','APROBADO'),(5,'LUCES','APROBADO'),(5,'NEUMATICOS','APROBADO'),(6,'FRENOS','APROBADO'),(6,'NEUMATICOS','OBSERVADO'),(6,'DIRECCION','APROBADO'),(6,'SUSPENSION','APROBADO'),(6,'LUCES','RECHAZADO'),(6,'EMISIONES','APROBADO'),(8,'FRENOS','APROBADO'),(8,'NEUMATICOS','APROBADO'),(8,'DIRECCION','APROBADO'),(8,'SUSPENSION','APROBADO'),(8,'LUCES','APROBADO'),(8,'EMISIONES','RECHAZADO'),(9,'FRENOS','APROBADO'),(9,'LUCES','APROBADO');
/*!40000 ALTER TABLE `inspeccion_pruebas` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `inspecciones_tecnicas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inspecciones_tecnicas` (
  `id_inspeccion` bigint NOT NULL AUTO_INCREMENT,
  `estado_proceso` varchar(30) DEFAULT NULL,
  `inspeccion_origen_id` bigint DEFAULT NULL,
  `inspector_id` bigint DEFAULT NULL,
  `supervisor_id` bigint DEFAULT NULL,
  `vehiculo_id` bigint NOT NULL,
  `observaciones` varchar(1000) DEFAULT NULL,
  `fecha_fin` datetime(6) DEFAULT NULL,
  `fecha_inicio` datetime(6) DEFAULT NULL,
  `resultado_condicion` varchar(30) DEFAULT NULL,
  `tipo_inspeccion` varchar(30) DEFAULT NULL,
  `acta_id` bigint DEFAULT NULL,
  `certificado_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id_inspeccion`),
  UNIQUE KEY `UKnhfe2gd9mni3wt3euct9aeidd` (`acta_id`),
  UNIQUE KEY `UK6knfog2c6c4mbvu040bbu51r7` (`certificado_id`),
  CONSTRAINT `FKabfq8cla4aeo1xu6p20xlxk84` FOREIGN KEY (`acta_id`) REFERENCES `actas_observaciones` (`id_acta`),
  CONSTRAINT `FKpvopwmu84514buhtua7aca5e2` FOREIGN KEY (`certificado_id`) REFERENCES `certificados_inspeccion` (`id_certificado`),
  CONSTRAINT `inspecciones_tecnicas_chk_1` CHECK ((`estado_proceso` in (_utf8mb4'REGISTRADA',_utf8mb4'EN_PROCESO',_utf8mb4'FINALIZADA'))),
  CONSTRAINT `inspecciones_tecnicas_chk_2` CHECK ((`resultado_condicion` in (_utf8mb4'APTO',_utf8mb4'OBSERVADO'))),
  CONSTRAINT `inspecciones_tecnicas_chk_3` CHECK ((`tipo_inspeccion` in (_utf8mb4'INSPECCION',_utf8mb4'REINSPECCION')))
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `inspecciones_tecnicas` WRITE;
/*!40000 ALTER TABLE `inspecciones_tecnicas` DISABLE KEYS */;
INSERT INTO `inspecciones_tecnicas` VALUES (1,'FINALIZADA',NULL,4,7,1,NULL,'2026-10-06 23:08:54.943181','2026-10-06 23:08:54.684178','APTO','INSPECCION',NULL,1),(2,'FINALIZADA',NULL,5,7,2,NULL,'2026-10-06 23:08:55.220963','2026-10-06 23:08:55.034786','APTO','INSPECCION',NULL,2),(3,'FINALIZADA',NULL,6,7,3,NULL,'2026-10-06 23:08:55.483565','2026-10-06 23:08:55.305270','APTO','INSPECCION',NULL,3),(4,'FINALIZADA',NULL,4,7,4,'Frenos con eficiencia por debajo del minimo; reparar y reinspeccionar.','2026-10-06 23:08:55.746520','2026-10-06 23:08:55.569968','OBSERVADO','INSPECCION',1,NULL),(5,'FINALIZADA',4,4,7,4,NULL,'2026-10-06 23:08:56.017023','2026-10-06 23:08:55.839241','APTO','REINSPECCION',NULL,4),(6,'FINALIZADA',NULL,5,7,5,'Faros desalineados y neumaticos con desgaste irregular.','2026-10-06 23:08:56.265301','2026-10-06 23:08:56.096103','OBSERVADO','INSPECCION',2,NULL),(7,'EN_PROCESO',6,5,7,5,NULL,NULL,'2026-10-06 23:08:56.357160',NULL,'REINSPECCION',NULL,NULL),(8,'FINALIZADA',NULL,6,NULL,6,'Emisiones sobre el limite permitido.','2026-10-06 23:08:56.578601','2026-10-06 23:08:56.412430','OBSERVADO','INSPECCION',3,NULL),(9,'EN_PROCESO',NULL,4,7,7,NULL,NULL,'2026-10-06 23:08:56.661447',NULL,'INSPECCION',NULL,NULL),(10,'EN_PROCESO',NULL,6,7,8,NULL,NULL,'2026-10-06 23:08:56.769166',NULL,'INSPECCION',NULL,NULL),(11,'REGISTRADA',NULL,5,7,9,NULL,NULL,NULL,NULL,'INSPECCION',NULL,NULL),(12,'REGISTRADA',NULL,6,NULL,10,NULL,NULL,NULL,NULL,'INSPECCION',NULL,NULL);
/*!40000 ALTER TABLE `inspecciones_tecnicas` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `rol_permisos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rol_permisos` (
  `rol_id` bigint NOT NULL,
  `permiso` varchar(100) NOT NULL,
  KEY `FK8y4iyfqu264k1e9dyn614ll1v` (`rol_id`),
  CONSTRAINT `FK8y4iyfqu264k1e9dyn614ll1v` FOREIGN KEY (`rol_id`) REFERENCES `roles_acceso` (`id_rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `rol_permisos` WRITE;
/*!40000 ALTER TABLE `rol_permisos` DISABLE KEYS */;
INSERT INTO `rol_permisos` VALUES (1,'USUARIOS_GESTIONAR'),(1,'ROLES_GESTIONAR'),(1,'CLIENTES_GESTIONAR'),(1,'INSPECCIONES_GESTIONAR'),(2,'CLIENTES_GESTIONAR'),(2,'VEHICULOS_GESTIONAR'),(2,'INSPECCIONES_REGISTRAR'),(3,'INSPECCIONES_INICIAR'),(3,'PRUEBAS_REGISTRAR'),(3,'INSPECCIONES_FINALIZAR'),(4,'INSPECCIONES_CONSULTAR'),(4,'INSPECCIONES_REVISAR');
/*!40000 ALTER TABLE `rol_permisos` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `roles_acceso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles_acceso` (
  `id_rol` bigint NOT NULL AUTO_INCREMENT,
  `estado_activo` bit(1) NOT NULL,
  `nombre_rol` enum('ROLE_ADMIN','ROLE_CLIENTE','ROLE_INSPECTOR','ROLE_MECANICO','ROLE_RECEPCIONISTA','ROLE_SUPERVISOR') NOT NULL,
  PRIMARY KEY (`id_rol`),
  UNIQUE KEY `uk_rol_nombre` (`nombre_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `roles_acceso` WRITE;
/*!40000 ALTER TABLE `roles_acceso` DISABLE KEYS */;
INSERT INTO `roles_acceso` VALUES (1,_binary '','ROLE_ADMIN'),(2,_binary '','ROLE_RECEPCIONISTA'),(3,_binary '','ROLE_INSPECTOR'),(4,_binary '','ROLE_SUPERVISOR');
/*!40000 ALTER TABLE `roles_acceso` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id_usuario` bigint NOT NULL AUTO_INCREMENT,
  `estado_activo` bit(1) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `rol_activo_id` bigint DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `uk_usuario_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,_binary '','RevTech2026!',1,'admin'),(2,_binary '','RevTech2026!',2,'recepcion.lucia'),(3,_binary '','RevTech2026!',2,'recepcion.marco'),(4,_binary '','RevTech2026!',3,'inspector.juan'),(5,_binary '','RevTech2026!',3,'inspector.rosa'),(6,_binary '','RevTech2026!',3,'inspector.pedro'),(7,_binary '','RevTech2026!',4,'supervisor.elena');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
DROP TABLE IF EXISTS `vehiculos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehiculos` (
  `id_vehiculo` bigint NOT NULL AUTO_INCREMENT,
  `anio_fabricacion` int DEFAULT NULL,
  `categoria` enum('L','M1','M2','M3','N1','N2','N3','O1','O2','O3','O4','OTRO') DEFAULT NULL,
  `cliente_id` bigint NOT NULL,
  `marca` varchar(50) DEFAULT NULL,
  `modelo` varchar(50) DEFAULT NULL,
  `placa` varchar(10) NOT NULL,
  PRIMARY KEY (`id_vehiculo`),
  UNIQUE KEY `uk_vehiculo_placa` (`placa`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `vehiculos` WRITE;
/*!40000 ALTER TABLE `vehiculos` DISABLE KEYS */;
INSERT INTO `vehiculos` VALUES (1,2019,'M1',1,'Toyota','Corolla','ABC-123'),(2,2022,'L',1,'Honda','CB190R','ABD-456'),(3,2016,'M1',2,'Hyundai','Accent','BCD-234'),(4,2018,'N1',3,'Nissan','Frontier','CDE-345'),(5,2020,'M1',4,'Kia','Rio','DEF-456'),(6,2015,'M2',5,'Toyota','Hiace','EFG-567'),(7,2012,'M1',6,'Suzuki','Swift','FGH-678'),(8,2017,'N3',7,'Volvo','FH16','GHI-789'),(9,2014,'M3',7,'Mercedes-Benz','O500','HIJ-890'),(10,2010,'O2',7,'Randon','SR','IJK-901'),(11,2021,'M1',8,'Mazda','CX-5','JKL-012'),(12,2008,'N2',8,'Hino','300','KLM-123');
/*!40000 ALTER TABLE `vehiculos` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

