-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: esportive
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `local`
--

DROP TABLE IF EXISTS `local`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `local` (
  `idLocal` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) DEFAULT NULL,
  `tipo` varchar(45) DEFAULT NULL,
  `cep` varchar(9) DEFAULT NULL,
  `numero` int DEFAULT NULL,
  `limite_por_dia` int DEFAULT NULL,
  `tempo_maximo` time DEFAULT NULL,
  `horario_abertura` time DEFAULT NULL,
  `horario_fechamento` time DEFAULT NULL,
  PRIMARY KEY (`idLocal`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `local`
--

LOCK TABLES `local` WRITE;
/*!40000 ALTER TABLE `local` DISABLE KEYS */;
INSERT INTO `local` VALUES (1,'Quadra CEEP','quadra','83501320',415,2,'02:00:00','08:00:00','18:00:00'),(2,'Buzatao','centro esportivo','84128422',13,1,'01:00:00','08:00:00','18:00:00'),(3,'Quadra Central','Quadra Poliesportiva','80000000',10,4,'03:00:00','09:00:00','19:00:00'),(4,'Ginásio Municipal','Ginásio','80001000',25,2,'02:00:00','07:00:00','17:00:00'),(5,'Campo de Futebol Vila Nova','Campo de Futebol','80002000',50,1,'02:00:00','10:00:00','20:00:00'),(6,'Parque das Águas','Parque','80003000',100,2,'01:00:00','06:00:00','22:00:00'),(7,'Piscina Pública Centro','Piscina','80004000',5,2,'02:00:00','11:00:00','21:00:00'),(8,'Auditório Cultural','Auditório','80005000',12,3,'02:00:00','08:00:00','18:00:00'),(9,'Sala de Eventos Jardim Europa','Sala de Eventos','80006000',8,1,'03:00:00','07:00:00','19:00:00'),(10,'Pista de Skate','Pista de Esportes','80007000',7,5,'02:00:00','12:00:00','23:00:00'),(11,'Centro de Convivência','Espaço Multiuso','80008000',20,10,'01:00:00','05:00:00','15:00:00'),(12,'Quadra de Areia','Quadra de Vôlei','80009000',15,2,'02:00:00','09:00:00','19:00:00');
/*!40000 ALTER TABLE `local` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-06 19:23:22
