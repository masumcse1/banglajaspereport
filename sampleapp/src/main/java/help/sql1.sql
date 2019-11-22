create database rptdb character set utf8 collate utf8_general_ci;



CREATE TABLE IF NOT EXISTS survey (
    id INT AUTO_INCREMENT,
    country_name VARCHAR(255) NOT NULL,
    population VARCHAR(255) NOT NULL,
    area VARCHAR(255) NOT NULL,
    
    PRIMARY KEY (id)
)  ENGINE=INNODB

character set utf8 collate utf8_general_ci;
