-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: localhost    Database: bddapp
-- ------------------------------------------------------
-- Server version       8.0.21

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

--
-- Table structure for table `departamento`
--

DROP TABLE IF EXISTS `departamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departamento` (
  `Id_departamento` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`Id_departamento`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departamento`
--

LOCK TABLES `departamento` WRITE;
/*!40000 ALTER TABLE `departamento` DISABLE KEYS */;
INSERT INTO `departamento` VALUES (1,'Jefatura'),(2,'Profesorado'),(3,'Conserje'),(4,'Deportes'),(5,'Admin');
/*!40000 ALTER TABLE `departamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dependencia`
--

DROP TABLE IF EXISTS `dependencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dependencia` (
  `id_dependencia` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  PRIMARY KEY (`id_dependencia`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dependencia`
--

LOCK TABLES `dependencia` WRITE;
/*!40000 ALTER TABLE `dependencia` DISABLE KEYS */;
INSERT INTO `dependencia` VALUES (1,'Aula1'),(2,'Aula2'),(3,'Sala de profesores'),(4,'Biblioteca'),(5,'Aula SMR1'),(6,'Aula SMR2');
/*!40000 ALTER TABLE `dependencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `libro`
--

DROP TABLE IF EXISTS `libro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `libro` (
  `Id_libro` int NOT NULL AUTO_INCREMENT,
  `Isbn` varchar(45) NOT NULL,
  `Titulo` varchar(60) NOT NULL,
  `Autor` varchar(60) DEFAULT NULL,
  `Editorial` varchar(50) NOT NULL,
  `Fecha_alta` date NOT NULL,
  `Fecha_baja` date DEFAULT NULL,
  `Departamento` int NOT NULL,
  `Foto` blob,
  `Ubicacion` int NOT NULL,
  PRIMARY KEY (`Id_libro`),
  KEY `libro.departamento_idx` (`Departamento`),
  KEY `libro.ubicacion_idx` (`Ubicacion`),
  CONSTRAINT `libro.departamento` FOREIGN KEY (`Departamento`) REFERENCES `departamento` (`Id_departamento`),
  CONSTRAINT `libro.ubicacion` FOREIGN KEY (`Ubicacion`) REFERENCES `dependencia` (`id_dependencia`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `libro`
--

LOCK TABLES `libro` WRITE;
/*!40000 ALTER TABLE `libro` DISABLE KEYS */;
INSERT INTO `libro` VALUES (1,'646446sa','Harryporter','Jk','editorial1','2022-04-01',NULL,2,NULL,1),(2,'6468','Lagrimas de shiva','pepe','editorial2','2022-04-02',NULL,2,NULL,2),(3,'649101','One Piece','paco','editorial3','2022-04-02',NULL,2,NULL,5);
/*!40000 ALTER TABLE `libro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `material`
--

DROP TABLE IF EXISTS `material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `material` (
  `Id_material` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  `Num_serie` varchar(45) NOT NULL,
  `Marca` varchar(45) NOT NULL,
  `Descripcion` varchar(45) DEFAULT NULL,
  `Fecha_alta` date NOT NULL,
  `Fecha_baja` date DEFAULT NULL,
  `Departamento` int NOT NULL,
  `Foto` blob,
  `Ubicacion` int NOT NULL,
  PRIMARY KEY (`Id_material`),
  KEY `material.departamento_idx` (`Departamento`),
  KEY `material.ubicacion_idx` (`Ubicacion`),
  CONSTRAINT `material.departamento` FOREIGN KEY (`Departamento`) REFERENCES `departamento` (`Id_departamento`),
  CONSTRAINT `material.ubicacion` FOREIGN KEY (`Ubicacion`) REFERENCES `dependencia` (`id_dependencia`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `material`
--

LOCK TABLES `material` WRITE;
/*!40000 ALTER TABLE `material` DISABLE KEYS */;
INSERT INTO `material` VALUES (1,'silla','1234','ikea','silla normal','2022-04-03',NULL,1,NULL,3),(2,'mesa','12345','ikea','mesa normal','2022-04-03',NULL,1,NULL,4);
/*!40000 ALTER TABLE `material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prestamo_libro`
--

DROP TABLE IF EXISTS `prestamo_libro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prestamo_libro` (
  `Id_prestamo_libro` int NOT NULL AUTO_INCREMENT,
  `Id_libro` int NOT NULL,
  `Id_profesor` int NOT NULL,
  `Fecha_prestamo` date NOT NULL,
  `Fecha_devolucion` date DEFAULT NULL,
  PRIMARY KEY (`Id_prestamo_libro`),
  KEY `prestamo.libro_idx` (`Id_libro`),
  KEY `prestamo.libro.profesor_idx` (`Id_profesor`),
  CONSTRAINT `prestamo.libro` FOREIGN KEY (`Id_libro`) REFERENCES `libro` (`Id_libro`),
  CONSTRAINT `prestamo.libro.profesor` FOREIGN KEY (`Id_profesor`) REFERENCES `profesor` (`Id_profesor`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prestamo_libro`
--

LOCK TABLES `prestamo_libro` WRITE;
/*!40000 ALTER TABLE `prestamo_libro` DISABLE KEYS */;
INSERT INTO `prestamo_libro` VALUES (1,1,1,'2022-04-01',NULL),(2,2,1,'2022-04-01',NULL),(3,3,3,'2022-04-01',NULL);
/*!40000 ALTER TABLE `prestamo_libro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prestamo_material`
--

DROP TABLE IF EXISTS `prestamo_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prestamo_material` (
  `Id_prestamo_material` int NOT NULL AUTO_INCREMENT,
  `Id_material` int NOT NULL,
  `Id_profesor` int NOT NULL,
  `Fecha_prestamo` date NOT NULL,
  `Fecha_devolucion` date DEFAULT NULL,
  PRIMARY KEY (`Id_prestamo_material`),
  KEY `prestamo.material_idx` (`Id_material`),
  KEY `prestamo.material.profesor_idx` (`Id_profesor`),
  CONSTRAINT `prestamo.material` FOREIGN KEY (`Id_material`) REFERENCES `material` (`Id_material`),
  CONSTRAINT `prestamo.material.profesor` FOREIGN KEY (`Id_profesor`) REFERENCES `profesor` (`Id_profesor`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prestamo_material`
--

LOCK TABLES `prestamo_material` WRITE;
/*!40000 ALTER TABLE `prestamo_material` DISABLE KEYS */;
INSERT INTO `prestamo_material` VALUES (1,1,1,'2022-04-01',NULL),(2,2,1,'2022-04-01',NULL),(3,2,1,'2022-04-02',NULL);
/*!40000 ALTER TABLE `prestamo_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profesor`
--

DROP TABLE IF EXISTS `profesor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profesor` (
  `Id_profesor` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(45) NOT NULL,
  `Apellidos` varchar(70) NOT NULL,
  `Correo` varchar(60) NOT NULL,
  `Password` varchar(20) NOT NULL,
  `Fecha_alta` date NOT NULL,
  `Fecha_baja` date DEFAULT NULL,
  `Departamento` int NOT NULL,
  `Rol` varchar(20) NOT NULL,
  PRIMARY KEY (`Id_profesor`),
  KEY `profesor.departamento_idx` (`Departamento`),
  CONSTRAINT `profesor.departamento` FOREIGN KEY (`Departamento`) REFERENCES `departamento` (`Id_departamento`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profesor`
--

LOCK TABLES `profesor` WRITE;
/*!40000 ALTER TABLE `profesor` DISABLE KEYS */;
INSERT INTO `profesor` VALUES (1,'paco','cervantes','paco@','1234','2022-04-03',NULL,2,'USER'),(2,'fernando','canales','fernando@','1234','2022-04-03',NULL,2,'USER'),(3,'jaime','altozano','jaime@','12345','2022-04-03',NULL,2,'USER'),(4,'luis','rosales','luis@','123123','1999-04-03',NULL,5,'ADMIN');
/*!40000 ALTER TABLE `profesor` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-05-02 20:41:50