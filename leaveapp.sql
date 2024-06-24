-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: leaveapp
-- ------------------------------------------------------
-- Server version	8.0.36

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
-- Table structure for table `compensation_claim`
--

DROP TABLE IF EXISTS `compensation_claim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `compensation_claim` (
  `id` int NOT NULL AUTO_INCREMENT,
  `compensation_ledger_id` int NOT NULL,
  `date` date NOT NULL,
  `employee_id` int NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `hours` double NOT NULL,
  `remarks` varchar(45) DEFAULT NULL,
  `status` enum('Applied','Updated','Deleted','Approved','Rejected','Canceled') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `compensation_claim`
--

LOCK TABLES `compensation_claim` WRITE;
/*!40000 ALTER TABLE `compensation_claim` DISABLE KEYS */;
INSERT INTO `compensation_claim` VALUES (1,3,'2024-01-16',3,'08:20:00','09:40:00',1.25,NULL,'Applied'),(2,4,'2024-05-09',4,'18:20:00','19:50:00',1.5,NULL,'Applied'),(3,3,'2024-06-24',3,'08:20:00','09:40:00',1.25,NULL,'Approved'),(4,4,'2024-01-15',4,'18:20:00','19:50:00',1.5,NULL,'Approved');
/*!40000 ALTER TABLE `compensation_claim` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `compensation_ledger`
--

DROP TABLE IF EXISTS `compensation_ledger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `compensation_ledger` (
  `id` int NOT NULL AUTO_INCREMENT,
  `employee_id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `compensation_ledger`
--

LOCK TABLES `compensation_ledger` WRITE;
/*!40000 ALTER TABLE `compensation_ledger` DISABLE KEYS */;
INSERT INTO `compensation_ledger` VALUES (1,1),(2,2),(3,3),(4,4),(5,5),(6,6);
/*!40000 ALTER TABLE `compensation_ledger` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `compensation_ledger_compensation_leave`
--

DROP TABLE IF EXISTS `compensation_ledger_compensation_leave`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `compensation_ledger_compensation_leave` (
  `compensation_leave_id` int NOT NULL,
  `compensation_ledger_id` int NOT NULL,
  PRIMARY KEY (`compensation_leave_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `compensation_ledger_compensation_leave`
--

LOCK TABLES `compensation_ledger_compensation_leave` WRITE;
/*!40000 ALTER TABLE `compensation_ledger_compensation_leave` DISABLE KEYS */;
/*!40000 ALTER TABLE `compensation_ledger_compensation_leave` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leave_quota`
--

DROP TABLE IF EXISTS `leave_quota`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leave_quota` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `annual_leave_quota` int NOT NULL,
  `medical_leave_quota` int NOT NULL,
  `user_year` varchar(45) NOT NULL,
  `role` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leave_quota`
--

LOCK TABLES `leave_quota` WRITE;
/*!40000 ALTER TABLE `leave_quota` DISABLE KEYS */;
INSERT INTO `leave_quota` VALUES (1,'Administrative',14,60,'2024','Manager'),(2,'Professional',18,60,'2024','Employee'),(3,'Professional',18,60,'2024','Manager'),(4,'Ceo',0,0,'0','Ceo');
/*!40000 ALTER TABLE `leave_quota` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leaveentity`
--

DROP TABLE IF EXISTS `leaveentity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leaveentity` (
  `id` int NOT NULL AUTO_INCREMENT,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `reason` varchar(45) NOT NULL,
  `status` enum('Applied','Updated','Deleted','Approved','Rejected','Canceled') DEFAULT NULL,
  `type` enum('Annual','Medical','Compensation') DEFAULT NULL,
  `work_dissemination` varchar(45) DEFAULT NULL,
  `contact` varchar(45) DEFAULT NULL,
  `duration` double DEFAULT NULL,
  `comment` varchar(45) DEFAULT NULL,
  `employee_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leaveentity`
--

LOCK TABLES `leaveentity` WRITE;
/*!40000 ALTER TABLE `leaveentity` DISABLE KEYS */;
INSERT INTO `leaveentity` VALUES (1,'2024-05-01','2024-05-05','Holidays','Approved','Annual',NULL,NULL,5,NULL,3),(2,'2024-06-24','2024-06-27','Overtime','Rejected','Compensation',NULL,NULL,3.5,NULL,3),(3,'2024-05-08','2024-05-09','Check up','Approved','Medical',NULL,NULL,2,'Have a good rest',3),(4,'2024-01-15','2024-01-16','Break','Approved','Annual',NULL,NULL,2,NULL,3),(5,'2024-05-01','2024-05-09','Going overseas	','Approved','Annual',NULL,NULL,7,NULL,5),(6,'2024-05-05','2024-05-09','Overtime','Approved','Compensation',NULL,NULL,5,NULL,4),(7,'2024-06-24','2024-06-27','Medical Appt','Rejected','Medical',NULL,NULL,4,NULL,5),(8,'2024-07-26','2024-07-27','Sick','Approved','Medical',NULL,NULL,2,NULL,3),(9,'2024-07-13','2024-07-15','Going overseas','Applied','Annual',NULL,NULL,3,NULL,3),(10,'2024-07-30','2024-07-31','long vacation','Applied','Annual','','',2,NULL,1),(11,'2024-06-22','2024-06-22','Secret0.0','Applied','Annual','','188888888',1,NULL,1);
/*!40000 ALTER TABLE `leaveentity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `public_holiday`
--

DROP TABLE IF EXISTS `public_holiday`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `public_holiday` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `public_holiday`
--

LOCK TABLES `public_holiday` WRITE;
/*!40000 ALTER TABLE `public_holiday` DISABLE KEYS */;
INSERT INTO `public_holiday` VALUES (1,'New Year\'s Day','2024-01-01'),(2,'Chinese New Year Day 1','2024-02-10'),(3,'Chinese New Year Day 2','2024-02-11'),(4,'Good Friday','2024-03-29');
/*!40000 ALTER TABLE `public_holiday` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `type` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `leave_quota_id` int DEFAULT NULL,
  `manager_id` int DEFAULT NULL,
  `ceo_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Paddington Bear','Manager','manager1','manager1',1,NULL,6),(2,'Agatha Christie','Manager','manager2','manager2',3,NULL,6),(3,'Hercule Poirot','Employee','employee1','employee1',2,1,NULL),(4,'Sherlock Holmes','Employee','employee2','employee2',2,1,NULL),(5,'Miss Marple	','Employee','employee3','employee3',2,1,NULL),(6,'Jay Gatsby','Ceo','ceo','ceo',4,NULL,NULL),(7,'Dr Watson','Admin','admin1','admin1',NULL,NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-22 14:42:14
