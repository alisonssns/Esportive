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
  `cpf` char(12) NOT NULL,
  `nome` varchar(100) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `senha` varchar(45) DEFAULT NULL,
  `Tipo` varchar(45) DEFAULT NULL,
  `rua` varchar(100) DEFAULT NULL,
  `bairro` varchar(45) DEFAULT NULL,
  `cidade` varchar(45) DEFAULT NULL,
  `cep` varchar(9) DEFAULT NULL,
  `estado` char(2) DEFAULT NULL,
  `numero` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`cpf`),
  KEY `idEnderecoUser_idx` (`rua`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES ('01234567890','Rafael','rafael123@gmail.com','senhaRafael','cli','Rua do Sol','Centro','Fortaleza','60000000','CE','400'),('1','1','alisoncracas3@gmail.com','lala1234',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('124739329-13','alisoncracas1@gmail.com','alisoncracas1@gmail.com','lala124',NULL,NULL,NULL,NULL,NULL,NULL,NULL),('12477766613','Alisson','alisoncracas2@gmail.com','lala1234','adm','São Bartolomeu','Monte Santo','Almirante Tamandaré','83501320','PR','415'),('23456789012','JoaoPessoa','joao123@gmail.com','joao123','cli','Rua das Flores','Centro','Curitiba','80000000','PR','100'),('34567890123','Maria','maria123@gmail.com','senhaMaria','cli','Av. Paulista','Bela Vista','São Paulo','01310100','SP','150'),('45678901234','Lucas','lucas123@gmail.com','senhaLucas','cli','Rua das Palmeiras','Jardim Botânico','Rio de Janeiro','22200000','RJ','20'),('56789012345','Ana','ana123@gmail.com','senhaAna','cli','Av. Sete de Setembro','Liberdade','Salvador','40000000','BA','300'),('67890123456','Paulo','paulo123@gmail.com','senhaPaulo','cli','Rua das Andorinhas','Jardim Europa','Porto Alegre','90000000','RS','50'),('78901234567','Clara','clara123@gmail.com','senhaClara','cli','Rua das Palmeiras','Copacabana','Rio de Janeiro','22000000','RJ','500'),('89012345678','Carlos','carlos123@gmail.com','senhaCarlos','cli','Av. Brasil','Centro','São Paulo','01000000','SP','900'),('90123456789','Sofia','sofia123@gmail.com','senhaSofia','cli','Rua dos Lírios','Jardim das Flores','Curitiba','81500000','PR','350');
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

-- Dump completed on 2024-12-15 22:13:48
