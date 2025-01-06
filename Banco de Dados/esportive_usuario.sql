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
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `cpf` char(11) NOT NULL,
  `nome` varchar(100) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `senha` varchar(45) DEFAULT NULL,
  `Tipo` varchar(45) DEFAULT NULL,
  `rua` varchar(100) DEFAULT NULL,
  `bairro` varchar(45) DEFAULT NULL,
  `cidade` varchar(45) DEFAULT NULL,
  `cep` int DEFAULT NULL,
  `estado` char(2) DEFAULT NULL,
  `numero` int DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`cpf`),
  KEY `idEnderecoUser_idx` (`rua`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES ('09019585908','Pedro','felipedasilvaandradeee@gmail.com','42016969','cli','uma ai','sim','colombo',8340579,'pr',1000,'Ativo'),('11355680905','Ashley Raissa','ashley.santos1808@gmail.com','11062023','cli','sao bart','sao jose','almirante tamandare',83501320,'PR',415,NULL),('12309845600','Alisson ','alisoncracas@gmail.com','lala1234','cli','SÆo bartolomeu','SÆo Roque','Almirante Tamandar‚',83501320,'PR',415,'Ativo'),('12345678909','João Silva','joao.silva@email.com','dF!4pLm1','cli','Rua das Flores','Centro','Pinhais',83320000,'PR',123,'Bloqueado'),('12473932913','Alis','alisoncracas2@gmail.com','lala1234','admin',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('14116701912','rodrigo','rodrigo@gmail.com','R10170270.','cli','mango','mango','cwb',12123,'pr',13,'Ativo'),('22043550209','Guilherme mizutani','guigui@gmail.com','lala1234','cli',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('23456788018','Maria Oliveira','maria.oliveira@email.com','gT#92jXw','cli','Avenida Brasil','Vila Nova','Curitiba',80010000,'PR',456,'Ativo'),('34567888730','Carlos Santos','carlos.santos@email.com','hY7!zWpQ','cli','Rua da Paz','Jardim Alegre','São Paulo',1010000,'SP',789,'Ativo'),('36884601035','Pedro Caralho','pedrocracas@gmail.com','lala1234','cli',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('45678989491','Ana Souza','ana.souza@email.com','rE%8kNvX','cli','Rua Primavera','Centro','Rio de Janeiro',20010000,'RJ',321,'Ativo'),('56789080595','Pedro Costa','pedro.costa@email.com','tM@6oLvY','cli','Rua das Palmeiras','Vila Velha','Salvador',40010000,'BA',654,'Ativo'),('67890181878','Mariana Lima','mariana.lima@email.com','vL*3qXpT','cli','Rua das Laranjeiras','Jardim Botânico','Belo Horizonte',3010000,'MG',987,'Ativo'),('78901282961','Lucas Almeida','lucas.almeida@email.com','wP$4rJyM','cli','Avenida Paulista','Bela Vista','São Paulo',1310000,'SP',135,'Ativo'),('89012383040','Fernanda Ferreira','fernanda.ferreira@email.com','xN&9sLyQ','cli','Rua dos Pássaros','Centro','Florianópolis',88010000,'SC',246,'Ativo'),('90123484122','Ricardo Martins','ricardo.martins@email.com','zK!7tMvP','cli','Rua das Acácias','Centro','Porto Alegre',9010000,'RS',357,'Ativo'),('99954165061','Roberta benvinda','aparecida@gmail.com','lala1234','cli',NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
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
