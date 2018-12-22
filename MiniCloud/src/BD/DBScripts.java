/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BD;

public interface DBScripts {

    static String DOES_DB_EXISTS = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = 'miniclouddb'";
    
    static String CREATION_SCRIPT = "-- MySQL Workbench Forward Engineering" +
    "" +
    "SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;" +
    "SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;" +
    "SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';" +
    "" +
    "-- -----------------------------------------------------" +
    "-- Schema MiniCloudDB" +
    "-- -----------------------------------------------------" +
    "" +
    "-- -----------------------------------------------------" +
    "-- Schema MiniCloudDB" +
    "-- -----------------------------------------------------" +
    "CREATE SCHEMA IF NOT EXISTS `MiniCloudDB` DEFAULT CHARACTER SET utf8 ;" +
    "USE `MiniCloudDB` ;" +
    "" +
    "-- -----------------------------------------------------" +
    "-- Table `MiniCloudDB`.`Users`" +
    "-- -----------------------------------------------------" +
    "CREATE TABLE IF NOT EXISTS `MiniCloudDB`.`Users` (" +
    "  `idUsers` INT NOT NULL," +
    "  `username` VARCHAR(30) NOT NULL," +
    "  `password` VARCHAR(50) NOT NULL," +
    "  PRIMARY KEY (`idUsers`)," +
    "  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE)" +
    "ENGINE = InnoDB;" +
    "" +
    "" +
    "-- -----------------------------------------------------" +
    "-- Table `MiniCloudDB`.`AuthUsers`" +
    "-- -----------------------------------------------------" +
    "CREATE TABLE IF NOT EXISTS `MiniCloudDB`.`AuthUsers` (" +
    "  `idAuthUsers` INT NOT NULL," +
    "  `userId` INT NOT NULL," +
    "  PRIMARY KEY (`idAuthUsers`, `userId`)," +
    "  INDEX `fk_AuthUsers_Users1_idx` (`userId` ASC) VISIBLE," +
    "  CONSTRAINT `fk_AuthUsers_Users1`" +
    "    FOREIGN KEY (`userId`)" +
    "    REFERENCES `MiniCloudDB`.`Users` (`idUsers`)" +
    "    ON DELETE NO ACTION" +
    "    ON UPDATE NO ACTION)" +
    "ENGINE = InnoDB;" +
    "" +
    "" +
    "-- -----------------------------------------------------" +
    "-- Table `MiniCloudDB`.`Files`" +
    "-- -----------------------------------------------------" +
    "CREATE TABLE IF NOT EXISTS `MiniCloudDB`.`Files` (" +
    "  `idFiles` INT NOT NULL," +
    "  `name` VARCHAR(254) NOT NULL," +
    "  `size` BIGINT NOT NULL," +
    "  `AuthUserId` INT NOT NULL," +
    "  PRIMARY KEY (`idFiles`, `AuthUserId`)," +
    "  INDEX `fk_Files_AuthUsers1_idx` (`AuthUserId` ASC) VISIBLE," +
    "  CONSTRAINT `fk_Files_AuthUsers1`" +
    "    FOREIGN KEY (`AuthUserId`)" +
    "    REFERENCES `MiniCloudDB`.`AuthUsers` (`idAuthUsers`)" +
    "    ON DELETE NO ACTION" +
    "    ON UPDATE NO ACTION)" +
    "ENGINE = InnoDB;" +
    "" +
    "" +
    "-- -----------------------------------------------------" +
    "-- Table `MiniCloudDB`.`History`" +
    "-- -----------------------------------------------------" +
    "CREATE TABLE IF NOT EXISTS `MiniCloudDB`.`History` (" +
    "  `idHistory` INT NOT NULL," +
    "  `source` INT NOT NULL," +
    "  `destination` INT NOT NULL," +
    "  `date` DATETIME NOT NULL," +
    "  `filename` VARCHAR(254) NOT NULL," +
    "  PRIMARY KEY (`idHistory`, `source`, `destination`)," +
    "  INDEX `fk_History_Users1_idx` (`source` ASC) VISIBLE," +
    "  INDEX `fk_History_Users2_idx` (`destination` ASC) VISIBLE," +
    "  CONSTRAINT `fk_History_Users1`" +
    "    FOREIGN KEY (`source`)" +
    "    REFERENCES `MiniCloudDB`.`Users` (`idUsers`)" +
    "    ON DELETE NO ACTION" +
    "    ON UPDATE NO ACTION," +
    "  CONSTRAINT `fk_History_Users2`" +
    "    FOREIGN KEY (`destination`)" +
    "    REFERENCES `MiniCloudDB`.`Users` (`idUsers`)" +
    "    ON DELETE NO ACTION" +
    "    ON UPDATE NO ACTION)" +
    "ENGINE = InnoDB;" +
    "" +
    "" +
    "SET SQL_MODE=@OLD_SQL_MODE;" +
    "SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;" +
    "SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;";
}
