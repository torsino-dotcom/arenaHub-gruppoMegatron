-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: db_arenahub
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `utenti_tornei`
--

DROP TABLE IF EXISTS `utenti_tornei`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utenti_tornei` (
  `id_utenti_tornei` bigint NOT NULL AUTO_INCREMENT,
  `id_utenti` bigint DEFAULT NULL,
  `id_tornei` bigint DEFAULT NULL,
  `data_iscrizione` date DEFAULT NULL,
  `stato` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_utenti_tornei`),
  KEY `FK6ow4vq4u9uk8vt1xpkwr2md37` (`id_tornei`),
  KEY `FKhdd8u2ufq5p7gfngotp7rylc3` (`id_utenti`),
  CONSTRAINT `FK6ow4vq4u9uk8vt1xpkwr2md37` FOREIGN KEY (`id_tornei`) REFERENCES `tornei` (`id_tornei`),
  CONSTRAINT `FKhdd8u2ufq5p7gfngotp7rylc3` FOREIGN KEY (`id_utenti`) REFERENCES `utenti` (`id_utenti`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utenti_tornei`
--

LOCK TABLES `utenti_tornei` WRITE;
/*!40000 ALTER TABLE `utenti_tornei` DISABLE KEYS */;
INSERT INTO `utenti_tornei` VALUES (1,1,1,'2026-10-02','confermato'),(2,2,1,'2026-10-03','confermato'),(3,4,1,'2026-10-04','confermato'),(4,5,1,'2026-10-05','confermato'),(5,6,1,'2026-10-06','confermato'),(6,7,1,'2026-10-07','in_attesa'),(7,8,1,'2026-10-08','annullato'),(8,1,2,'2026-09-16','confermato'),(9,4,2,'2026-09-17','confermato'),(10,5,2,'2026-09-18','confermato'),(11,6,2,'2026-09-19','confermato'),(12,9,3,'2026-08-02','confermato'),(13,8,3,'2026-08-03','confermato'),(14,1,4,'2026-09-21','confermato'),(15,2,4,'2026-09-22','confermato'),(16,7,4,'2026-09-23','in_attesa'),(17,11,1,'2026-10-14','confermato');
/*!40000 ALTER TABLE `utenti_tornei` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-15  9:23:57
