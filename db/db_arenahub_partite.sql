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
-- Table structure for table `partite`
--

DROP TABLE IF EXISTS `partite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `partite` (
  `id_partite` bigint NOT NULL AUTO_INCREMENT,
  `id_tornei` bigint DEFAULT NULL,
  `id_utenti1` bigint DEFAULT NULL,
  `id_utenti2` bigint DEFAULT NULL,
  `orario_inizio` datetime DEFAULT NULL,
  `punteggio_1` int DEFAULT NULL,
  `punteggio_2` int DEFAULT NULL,
  `id_persona_vincitrice` bigint DEFAULT NULL,
  `formato_match` varchar(255) DEFAULT NULL,
  `stato` varchar(255) DEFAULT NULL,
  `fase` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_partite`),
  KEY `FKg5f1axrtht8c17lkt4v6scjuy` (`id_utenti1`),
  KEY `FKtrsvufdrp9951ghgv3lpayja5` (`id_utenti2`),
  KEY `FK601t7an55ulds1wy61hhvi0xu` (`id_tornei`),
  CONSTRAINT `FK601t7an55ulds1wy61hhvi0xu` FOREIGN KEY (`id_tornei`) REFERENCES `tornei` (`id_tornei`),
  CONSTRAINT `FKg5f1axrtht8c17lkt4v6scjuy` FOREIGN KEY (`id_utenti1`) REFERENCES `utenti` (`id_utenti`),
  CONSTRAINT `FKtrsvufdrp9951ghgv3lpayja5` FOREIGN KEY (`id_utenti2`) REFERENCES `utenti` (`id_utenti`)
) ENGINE=InnoDB AUTO_INCREMENT=91007 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `partite`
--

LOCK TABLES `partite` WRITE;
/*!40000 ALTER TABLE `partite` DISABLE KEYS */;
INSERT INTO `partite` VALUES (91001,2,1,4,'2026-10-14 17:00:00',1,2,4,'BO3','conclusa','Semifinale'),(91002,2,5,6,'2026-10-14 17:30:00',0,2,6,'BO3','conclusa','Semifinale'),(91003,2,4,6,'2026-10-14 18:00:00',1,0,NULL,'BO5','in_corso','Finale'),(91005,4,1,2,'2026-10-25 18:00:00',NULL,NULL,NULL,'BO3','programmata','Quarti di finale'),(91006,3,9,8,'2026-10-23 21:34:00',0,0,NULL,'bo2','programmata','seminifinale');
/*!40000 ALTER TABLE `partite` ENABLE KEYS */;
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
