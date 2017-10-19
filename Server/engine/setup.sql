-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: osam
-- ------------------------------------------------------
-- Server version	5.7.19-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `jongyeob_tb_benefit_info`
--

DROP TABLE IF EXISTS `jongyeob_tb_benefit_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jongyeob_tb_benefit_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company` varchar(256) DEFAULT NULL,
  `latitude` float(14,10) DEFAULT NULL,
  `longitude` float(14,10) DEFAULT NULL,
  `area` varchar(256) DEFAULT NULL,
  `classification` int(11) DEFAULT NULL,
  `info` varchar(256) DEFAULT NULL,
  `date` datetime DEFAULT CURRENT_TIMESTAMP,
  `detail_address` varchar(256) DEFAULT NULL,
  `detail_info` text,
  `company_info` text,
  `company_tel` text,
  `user_no` int(11) DEFAULT NULL,
  `img_url` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jongyeob_tb_benefit_info`
--

LOCK TABLES `jongyeob_tb_benefit_info` WRITE;
/*!40000 ALTER TABLE `jongyeob_tb_benefit_info` DISABLE KEYS */;
INSERT INTO `jongyeob_tb_benefit_info` VALUES (5,'GS25 장대이수점',36.3624458313,127.3394927979,' 대전광역시 유성구 문화원로19번길 34-4',1,'나라사랑카드 10% 할인','2017-10-20 01:06:51',' 대전광역시 유성구 문화원로19번길 34-4','나라사랑카드 사용시 전 품목 10% 할인','GS25 편의점입니다.','042-826-2419',0,'/uploads/596486453053289.jpg'),(6,'VIPS 유성점',36.3637351990,127.3381576538,' 대전광역시 유성구 유성대로 808',2,'25% 할인','2017-10-20 01:10:44',' 대전광역시 유성구 유성대로 808','제휴 할인 + 15%할인 / 제휴 미할인시 25%할인 ','스테이크 하우스, 스테이크, 샐러드 바','042-832-4842',0,'/uploads/540144974732218.jpg'),(7,'롯데시네마 유성점',36.3612480164,127.3364944458,' 대전광역시 유성구 유성대로 776',5,'영화 6000원','2017-10-20 01:12:05',' 대전광역시 유성구 유성대로 776','2D 6000원 3D 10000원','즐거운 영화!','042-624-4154',0,'/uploads/1009226457029177.png'),(8,'이니스프리',36.3651885986,127.3371582031,' 대전광역시 유성구 유성대로811번길 20',1,'10% 할이','2017-10-20 01:13:35',' 대전광역시 유성구 유성대로811번길 20','IBK 카드로 남자화장품만 가능','화장품 로드샵','042-454-4852',0,'/uploads/519403687718399.jpg'),(9,'CGV 대전',36.3209800720,127.4089660645,' 대전광역시 중구 계백로 1700',5,'영화 6000원','2017-10-20 01:15:24',' 대전광역시 중구 계백로 1700','영화할인을 누리세요','영화왕','042-684-1181',0,'/uploads/1032842007320058.jpg'),(10,'국군복지단 대천 콘도',36.3020591736,126.5228195190,' 충청남도 보령시 갓바위1길',3,'콘도 할인','2017-10-20 01:17:44',' 충청남도 보령시 갓바위1길','살려줘','대한민국국군입니다.','043-465--7410',0,'/uploads/936389627559006.png'),(11,'국군복지단 서귀포 호텔',33.2731895447,126.3947296143,' 제주특별자치도 서귀포시 상예로 319',3,'호텔 할인','2017-10-20 01:19:37',' 제주특별자치도 서귀포시 상예로 319','윤성이네민박','어서옵서예','043-651-4523',0,'/uploads/285796398840506.png'),(13,'GS25 충남대학사점',36.3620529175,127.3431472778,' 대전광역시 유성구 궁동 451-7',1,'나라사랑카드10%할인','2017-10-20 02:09:27',' 대전광역시 유성구 궁동 451-7','할인뿅뿅','최고의편의점','042-848-7425',0,'/uploads/632768858350750.jpg'),(14,'CGV 강남',37.4952125549,127.0331878662,' 서울특별시 서초구 역삼1동 강남대로 438',5,'영화 6000원','2017-10-20 02:10:46',' 서울특별시 서초구 역삼1동 강남대로 438','싸다싸','김영화(22세,무직)','02-548-5256',0,'/uploads/782953187433897.jpg'),(15,'T',37.5665359497,126.9779663086,' Seoul',1,'P','2017-10-20 02:37:34',' Seoul','A','M','031-344-6655',0,'/images/default.png');
/*!40000 ALTER TABLE `jongyeob_tb_benefit_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jongyeob_tb_classification_code`
--

DROP TABLE IF EXISTS `jongyeob_tb_classification_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jongyeob_tb_classification_code` (
  `code` int(11) DEFAULT NULL,
  `classify_name` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jongyeob_tb_classification_code`
--

LOCK TABLES `jongyeob_tb_classification_code` WRITE;
/*!40000 ALTER TABLE `jongyeob_tb_classification_code` DISABLE KEYS */;
INSERT INTO `jongyeob_tb_classification_code` VALUES (1,'마트'),(2,'외식'),(3,'숙박'),(4,'여행'),(5,'여가');
/*!40000 ALTER TABLE `jongyeob_tb_classification_code` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-10-20  2:47:54
