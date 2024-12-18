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
  `horario` time DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `idLocal` int DEFAULT NULL,
  `cpfUsuario` char(12) DEFAULT NULL,
  PRIMARY KEY (`idreserva`),
  KEY `idLocalReserva_idx` (`idLocal`),
  KEY `cpfUsuarioReserva_idx` (`cpfUsuario`),
  CONSTRAINT `cpfUsuarioReserva` FOREIGN KEY (`cpfUsuario`) REFERENCES `usuario` (`cpf`),
  CONSTRAINT `idLocalReserva` FOREIGN KEY (`idLocal`) REFERENCES `local` (`idLocal`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reserva`
--

LOCK TABLES `reserva` WRITE;
/*!40000 ALTER TABLE `reserva` DISABLE KEYS */;
INSERT INTO `reserva` VALUES (5,'2024-12-15','10:00:00','Confirmada',1,'12477766613'),(6,'2024-12-15','14:00:00','Pendente',3,'23456789012'),(7,'2024-12-16','08:00:00','Confirmada',6,'34567890123'),(8,'2024-12-16','18:00:00','Cancelada',2,'45678901234'),(9,'2024-12-17','16:00:00','Confirmada',3,'56789012345'),(10,'2024-12-17','09:00:00','Pendente',5,'67890123456'),(11,'2024-12-18','20:00:00','Confirmada',3,'78901234567'),(12,'2024-12-18','11:00:00','Cancelada',6,'89012345678'),(13,'2024-12-19','15:00:00','Confirmada',7,'90123456789'),(14,'2024-12-19','13:00:00','Pendente',1,'01234567890'),(16,'2024-12-19','10:30:00','PENDENTE',4,'01234567890'),(18,'2025-12-20','10:10:00','PENDENTE',11,'23456789012'),(19,'2024-12-12','19:30:00','PENDENTE',12,'23456789012'),(20,'2024-12-16','02:10:00','PENDENTE',5,'23456789012');
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

-- Dump completed on 2024-12-15 22:13:49
