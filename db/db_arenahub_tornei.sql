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
-- Table structure for table `tornei`
--

DROP TABLE IF EXISTS `tornei`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tornei` (
  `id_tornei` bigint NOT NULL AUTO_INCREMENT,
  `nome_torneo` varchar(45) DEFAULT NULL,
  `descrizione` varchar(1000) DEFAULT NULL,
  `id_giochi` bigint DEFAULT NULL,
  `data_ora` datetime DEFAULT NULL,
  `data_inizio_iscrizioni` date DEFAULT NULL,
  `data_fine_iscrizioni` date DEFAULT NULL,
  `stato` varchar(255) DEFAULT NULL,
  `posti_max` int DEFAULT NULL,
  `url_img` varchar(255) DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_tornei`),
  KEY `FK7ds4cai1f90rud2sk6oc0qowu` (`id_giochi`),
  CONSTRAINT `FK7ds4cai1f90rud2sk6oc0qowu` FOREIGN KEY (`id_giochi`) REFERENCES `giochi` (`id_giochi`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tornei`
--

LOCK TABLES `tornei` WRITE;
/*!40000 ALTER TABLE `tornei` DISABLE KEYS */;
INSERT INTO `tornei` VALUES (1,'Arena Cup Valorant','Torneo Valorant 5v5',1,'2026-11-07 15:00:00','2026-10-01','2026-10-30','aperto',8,'valorant.jpg',NULL),(2,'Rocket League Showdown','Coppa a eliminazione diretta',2,'2026-10-14 18:00:00','2026-09-15','2026-10-12','in_corso',8,'rocket.jpg',NULL),(3,'Street Fighter Cup','Finale Street Fighter 6',3,'2026-09-20 16:00:00','2026-08-01','2026-09-10','concluso',16,'sf6.jpg',NULL),(4,'FC Weekend Cup','Torneo EA SPORTS FC 26',4,'2026-10-25 17:00:00','2026-09-20','2026-10-10','chiuso',16,'fc26.jpg',NULL),(5,'Apex Legends Draft','Torneo Apex Legends',5,'2026-12-06 15:00:00','2026-11-01','2026-11-30','bozza',20,'apex.jpg',NULL);
/*!40000 ALTER TABLE `tornei` ENABLE KEYS */;
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
