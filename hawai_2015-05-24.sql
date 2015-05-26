# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: localhost (MySQL 10.0.19-MariaDB)
# Datenbank: hawai
# Erstellungsdauer: 2015-05-24 16:02:21 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Export von Tabelle Application
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Application`;

CREATE TABLE `Application` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client_id` varchar(255) DEFAULT NULL,
  `scopes` varchar(255) DEFAULT NULL,
  `client_secret` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Export von Tabelle Application_HawaiAuthority
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Application_HawaiAuthority`;

CREATE TABLE `Application_HawaiAuthority` (
  `Application_id` int(11) NOT NULL,
  `authorities_authority_name` varchar(255) NOT NULL,
  KEY `FK_49h2jhm8iv33a1b3hxg2et9ft` (`authorities_authority_name`),
  KEY `FK_fo5k0t1doobn71coqkgtg4b4t` (`Application_id`),
  CONSTRAINT `FK_49h2jhm8iv33a1b3hxg2et9ft` FOREIGN KEY (`authorities_authority_name`) REFERENCES `HawaiAuthority` (`authority_name`),
  CONSTRAINT `FK_fo5k0t1doobn71coqkgtg4b4t` FOREIGN KEY (`Application_id`) REFERENCES `Application` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Export von Tabelle Bike
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Bike`;

CREATE TABLE `Bike` (
  `id` binary(16) NOT NULL,
  `version` int(11) NOT NULL,
  `frame_number` bigint(20) NOT NULL,
  `mileage` double DEFAULT NULL,
  `next_maintenance` date DEFAULT NULL,
  `buy_date` date NOT NULL,
  `type` varchar(50) DEFAULT NULL,
  `owner_id` binary(16) DEFAULT NULL,
  `soldLocation_id` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_59p68nb6oy9m3321a548v84au` (`owner_id`),
  KEY `FK_bcl93o9hq1njgxsoikgy0d5vh` (`soldLocation_id`),
  CONSTRAINT `FK_59p68nb6oy9m3321a548v84au` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_bcl93o9hq1njgxsoikgy0d5vh` FOREIGN KEY (`soldLocation_id`) REFERENCES `SellingLocation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Export von Tabelle HawaiAuthority
# ------------------------------------------------------------

DROP TABLE IF EXISTS `HawaiAuthority`;

CREATE TABLE `HawaiAuthority` (
  `authority_name` varchar(255) NOT NULL,
  PRIMARY KEY (`authority_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Export von Tabelle SellingLocation
# ------------------------------------------------------------

DROP TABLE IF EXISTS `SellingLocation`;

CREATE TABLE `SellingLocation` (
  `id` binary(16) NOT NULL,
  `version` int(11) NOT NULL,
  `address_city` varchar(50) NOT NULL,
  `address_country` varchar(30) NOT NULL,
  `address_house_number` varchar(10) NOT NULL,
  `address_postcode` varchar(10) NOT NULL,
  `address_state` varchar(50) NOT NULL,
  `address_street` varchar(250) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Export von Tabelle tour
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tour`;

CREATE TABLE `tour` (
  `id` binary(16) NOT NULL,
  `version` int(11) NOT NULL,
  `createdAt` date NOT NULL,
  `finshedAt` date NOT NULL,
  `lengthInKm` double NOT NULL,
  `name` varchar(50) NOT NULL,
  `startAt` date NOT NULL,
  `updatedAt` date NOT NULL,
  `bike_id` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_l58hlo9lndc0wgn433by2irss` (`bike_id`),
  CONSTRAINT `FK_l58hlo9lndc0wgn433by2irss` FOREIGN KEY (`bike_id`) REFERENCES `Bike` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Export von Tabelle Tour_waypoints
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Tour_waypoints`;

CREATE TABLE `Tour_waypoints` (
  `Tour_id` binary(16) NOT NULL,
  `gps_latitude` double NOT NULL,
  `gps_longitude` double NOT NULL,
  `gps_name` varchar(255) NOT NULL,
  KEY `FK_91dh9xsffh4awahgpeb4nau4s` (`Tour_id`),
  CONSTRAINT `FK_91dh9xsffh4awahgpeb4nau4s` FOREIGN KEY (`Tour_id`) REFERENCES `tour` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Export von Tabelle user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` binary(16) NOT NULL,
  `version` int(11) NOT NULL,
  `address_city` varchar(50) NOT NULL,
  `address_country` varchar(30) NOT NULL,
  `address_house_number` varchar(10) NOT NULL,
  `address_postcode` varchar(10) NOT NULL,
  `address_state` varchar(50) NOT NULL,
  `address_street` varchar(250) NOT NULL,
  `user_type` tinyblob NOT NULL,
  `birthdate` date DEFAULT NULL,
  `firstname` varchar(30) NOT NULL,
  `email_address` varchar(250) NOT NULL,
  `name` varchar(30) NOT NULL,
  `password` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_d0ar1h7wcp7ldy6qg5859sol6` (`email_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
