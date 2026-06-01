-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: blackjack
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.32-MariaDB

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
-- Table structure for table `carta`
--

DROP TABLE IF EXISTS `carta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carta` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `valor` varchar(5) NOT NULL,
  `palo` varchar(20) NOT NULL,
  `puntos` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carta`
--

LOCK TABLES `carta` WRITE;
/*!40000 ALTER TABLE `carta` DISABLE KEYS */;
INSERT INTO `carta` VALUES (1,'A','♥',11),(2,'2','♥',2),(3,'3','♥',3),(4,'4','♥',4),(5,'5','♥',5),(6,'6','♥',6),(7,'7','♥',7),(8,'8','♥',8),(9,'9','♥',9),(10,'10','♥',10),(11,'J','♥',10),(12,'Q','♥',10),(13,'K','♥',10),(14,'A','♦',11),(15,'2','♦',2),(16,'3','♦',3),(17,'4','♦',4),(18,'5','♦',5),(19,'6','♦',6),(20,'7','♦',7),(21,'8','♦',8),(22,'9','♦',9),(23,'10','♦',10),(24,'J','♦',10),(25,'Q','♦',10),(26,'K','♦',10),(27,'A','♣',11),(28,'2','♣',2),(29,'3','♣',3),(30,'4','♣',4),(31,'5','♣',5),(32,'6','♣',6),(33,'7','♣',7),(34,'8','♣',8),(35,'9','♣',9),(36,'10','♣',10),(37,'J','♣',10),(38,'Q','♣',10),(39,'K','♣',10),(40,'A','♠',11),(41,'2','♠',2),(42,'3','♠',3),(43,'4','♠',4),(44,'5','♠',5),(45,'6','♠',6),(46,'7','♠',7),(47,'8','♠',8),(48,'9','♠',9),(49,'10','♠',10),(50,'J','♠',10),(51,'Q','♠',10),(52,'K','♠',10);
/*!40000 ALTER TABLE `carta` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-01 10:37:10
