/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BD;

public interface DBScripts {
    
    //Check if database named miniclouddb exists within the mysql server.
    final static String DOES_DB_EXISTS = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = 'miniclouddb'";
    
    //Database creation scripts
    final static String CREATE_DATABASE =
            "CREATE SCHEMA IF NOT EXISTS `MiniCloudDB` DEFAULT CHARACTER SET utf8;";
    
    final static String CREATE_TABLE_USERS = 
            "CREATE TABLE IF NOT EXISTS `MiniCloudDB`.`Users` (" +
                "`idUser` INT NOT NULL AUTO_INCREMENT," +
                "`username` VARCHAR(30) NOT NULL," +
                "`password` VARCHAR(50) NOT NULL," +
                "PRIMARY KEY (`idUser`)," +
                "UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE)" +
            "ENGINE = InnoDB;";
    
    final static String CREATE_TABLE_AUTH_USERS =
            "CREATE TABLE IF NOT EXISTS `MiniCloudDB`.`AuthUsers` (" +
            "  `userId` INT NOT NULL," +
            "  `keepAlivePort` INT NOT NULL," +
            "  `transferPort` INT NOT NULL," +
            "  `notificationPort` INT NOT NULL," +
            "  `ipAddress` VARCHAR(15) NOT NULL," +
            "  `strikes` INT UNSIGNED NOT NULL DEFAULT 0," +
            "  PRIMARY KEY (`userId`)," +
            "  INDEX `fk_AuthUsers_Users1_idx` (`userId` ASC) VISIBLE," +
            "  CONSTRAINT `fk_AuthUsers_Users1`" +
            "    FOREIGN KEY (`userId`)" +
            "    REFERENCES `MiniCloudDB`.`Users` (`idUser`) "+
            "    ON DELETE CASCADE" +
            "    ON UPDATE NO ACTION)" +
            "ENGINE = InnoDB;";
    
    final static String CREATE_TABLE_FILES = 
            "CREATE TABLE IF NOT EXISTS `MiniCloudDB`.`Files` (" +
            "  `idFiles` INT NOT NULL AUTO_INCREMENT," +
            "  `owner` INT NOT NULL," +
            "  `name` VARCHAR(254) NOT NULL," +
            "  `size` BIGINT NOT NULL," +
            "  PRIMARY KEY (`idFiles`, `owner`)," +
            "  INDEX `fk_Files_AuthUsers1_idx` (`owner` ASC) VISIBLE," +
            "  CONSTRAINT `fk_Files_AuthUsers1`" +
            "    FOREIGN KEY (`owner`)" +
            "    REFERENCES `MiniCloudDB`.`AuthUsers` (`userId`)" +
            "    ON DELETE CASCADE" +
            "    ON UPDATE NO ACTION)" +
            "ENGINE = InnoDB";
    
    
    final static String CREATE_TABLE_HISTORY = 
            "CREATE TABLE IF NOT EXISTS `MiniCloudDB`.`History` (" +
            "  `idHistory` INT NOT NULL AUTO_INCREMENT," +
            "  `source` INT NOT NULL," +
            "  `destination` INT NOT NULL," +
            "  `date` DATETIME NOT NULL," +
            "  `filename` VARCHAR(254) NOT NULL," +
            "  PRIMARY KEY (`idHistory`,`source`, `destination`)," +
            "  INDEX `fk_History_Users1_idx` (`source` ASC) VISIBLE," +
            "  INDEX `fk_History_Users2_idx` (`destination` ASC) VISIBLE," +
            "  CONSTRAINT `fk_History_Users1`" +
            "    FOREIGN KEY (`source`)" +
            "    REFERENCES `MiniCloudDB`.`Users` (`idUser`)" +
            "    ON DELETE NO ACTION" +
            "    ON UPDATE NO ACTION," +
            "  CONSTRAINT `fk_History_Users2`" +
            "    FOREIGN KEY (`destination`)" +
            "    REFERENCES `MiniCloudDB`.`Users` (`idUser`)" +
            "    ON DELETE NO ACTION" +
            "    ON UPDATE NO ACTION)" +
            "ENGINE = InnoDB;";
}
