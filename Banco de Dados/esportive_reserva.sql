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
-- Table structure for table `reserva`
--

DROP TABLE IF EXISTS `reserva`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reserva` (
  `idreserva` int NOT NULL AUTO_INCREMENT,
  `data` date DEFAULT NULL,
  `horario_inicio` time DEFAULT NULL,
  `horario_fim` time DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `data_registro` date DEFAULT NULL,
  `hora_registro` time DEFAULT NULL,
  `idLocal` int DEFAULT NULL,
  `cpfUsuario` char(12) DEFAULT NULL,
  PRIMARY KEY (`idreserva`),
  KEY `idLocalReserva_idx` (`idLocal`),
  CONSTRAINT `idLocalReserva` FOREIGN KEY (`idLocal`) REFERENCES `local` (`idLocal`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reserva`
--

LOCK TABLES `reserva` WRITE;
/*!40000 ALTER TABLE `reserva` DISABLE KEYS */;
INSERT INTO `reserva` VALUES (1,'2024-12-21','10:00:00','12:00:00',NULL,NULL,NULL,1,'12309845600'),(2,'2024-12-22','16:00:00','17:00:00','PENDENTE',NULL,NULL,1,'12309845600'),(3,'2024-12-25','12:00:00','14:00:00','PENDENTE',NULL,NULL,1,'12309845600'),(4,'2025-12-12','10:00:00','15:00:00','PENDENTE',NULL,NULL,1,'12309845600'),(5,'2024-12-22','08:00:00','09:00:00','PENDENTE',NULL,NULL,1,'12309845600'),(6,'2024-12-22','07:00:00','08:00:00','PENDENTE',NULL,NULL,11,'12309845600'),(7,'2024-12-23','09:00:00','10:00:00','PENDENTE',NULL,NULL,11,'12309845600'),(8,'2025-05-11','09:00:00','10:30:00','PENDENTE',NULL,NULL,1,'12309845600'),(9,'2025-12-12','10:00:00','11:30:00','PENDENTE',NULL,NULL,4,'12309845600'),(10,'2025-12-12','05:00:00','06:00:00','PENDENTE','2024-12-27','00:03:29',11,'12309845600'),(11,'2025-12-12','08:00:00','10:00:00','PENDENTE','2024-12-27','00:03:44',1,'12309845600'),(12,'2025-12-12','09:00:00','10:30:00','PENDENTE','2024-12-27','00:13:56',12,'12309845600'),(13,'2025-12-12','11:00:00','12:45:00','PENDENTE','2024-12-26','23:18:31',12,'12309845600'),(14,'2025-12-12','06:00:00','01:00:00','PENDENTE','2024-12-27','12:18:32',11,'12309845600'),(15,'2025-12-12','15:00:00','09:38:00','PENDENTE','2024-12-27','12:18:49',1,'12309845600'),(16,'2025-12-12','08:00:00','09:39:00','PENDENTE','2024-12-27','13:15:59',8,'12309845600'),(17,'2025-05-23','11:00:00','11:30:00','PENDENTE','2024-12-27','14:17:47',7,'12309845600'),(18,'2024-12-29','15:00:00','16:00:00','Cancelada','2024-12-27','12:18:38',2,'12309845600'),(19,'2025-11-12','05:00:00','06:00:00','PENDENTE','2024-12-29','15:13:22',11,'12473932913'),(20,'2025-01-10','22:00:00','23:00:00','CANCELADA','2025-01-06','17:59:13',10,'09019585908');
/*!40000 ALTER TABLE `reserva` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-06 19:23:21
