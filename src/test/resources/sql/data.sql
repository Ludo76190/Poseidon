CREATE DATABASE if not exists poseidontest;

use poseidontest;

DROP TABLE if exists `bidlist`;
DROP TABLE if exists `trade`;
DROP TABLE if exists `rating`;
DROP TABLE if exists `curvepoint`;
DROP TABLE if exists `rulename`;
DROP TABLE if exists `users`;


CREATE TABLE BidList (
                         BidListId tinyint(4) NOT NULL AUTO_INCREMENT,
                         account VARCHAR(30) NOT NULL,
                         type VARCHAR(30) NOT NULL,
                         bidQuantity DOUBLE,
                         askQuantity DOUBLE,
                         bid DOUBLE ,
                         ask DOUBLE,
                         benchmark VARCHAR(125),
                         bidListDate TIMESTAMP,
                         commentary VARCHAR(125),
                         security VARCHAR(125),
                         status VARCHAR(10),
                         trader VARCHAR(125),
                         book VARCHAR(125),
                         creationName VARCHAR(125),
                         creationDate TIMESTAMP ,
                         revisionName VARCHAR(125),
                         revisionDate TIMESTAMP ,
                         dealName VARCHAR(125),
                         dealType VARCHAR(125),
                         sourceListId VARCHAR(125),
                         side VARCHAR(125),

                         PRIMARY KEY (BidListId)
)
    ENGINE=InnoDB ;

CREATE TABLE Trade (
                       TradeId tinyint(4) NOT NULL AUTO_INCREMENT,
                       account VARCHAR(30) NOT NULL,
                       type VARCHAR(30) NOT NULL,
                       buyQuantity DOUBLE,
                       sellQuantity DOUBLE,
                       buyPrice DOUBLE ,
                       sellPrice DOUBLE,
                       tradeDate TIMESTAMP,
                       security VARCHAR(125),
                       status VARCHAR(10),
                       trader VARCHAR(125),
                       benchmark VARCHAR(125),
                       book VARCHAR(125),
                       creationName VARCHAR(125),
                       creationDate TIMESTAMP ,
                       revisionName VARCHAR(125),
                       revisionDate TIMESTAMP ,
                       dealName VARCHAR(125),
                       dealType VARCHAR(125),
                       sourceListId VARCHAR(125),
                       side VARCHAR(125),

                       PRIMARY KEY (TradeId)
)
    ENGINE=InnoDB ;

CREATE TABLE CurvePoint (
                            Id tinyint(4) NOT NULL AUTO_INCREMENT,
                            CurveId tinyint,
                            asOfDate TIMESTAMP,
                            term DOUBLE ,
                            value DOUBLE ,
                            creationDate TIMESTAMP ,

                            PRIMARY KEY (Id)
)
    ENGINE=InnoDB ;

CREATE TABLE Rating (
                        Id tinyint(4) NOT NULL AUTO_INCREMENT,
                        moodysRating VARCHAR(125),
                        sandPRating VARCHAR(125),
                        fitchRating VARCHAR(125),
                        orderNumber tinyint,

                        PRIMARY KEY (Id)
)
    ENGINE=InnoDB ;

CREATE TABLE RuleName (
                          Id tinyint(4) NOT NULL AUTO_INCREMENT,
                          name VARCHAR(125),
                          description VARCHAR(125),
                          json VARCHAR(125),
                          template VARCHAR(512),
                          sqlStr VARCHAR(125),
                          sqlPart VARCHAR(125),

                          PRIMARY KEY (Id)
)
    ENGINE=InnoDB ;

CREATE TABLE Users (
                       Id tinyint(4) NOT NULL AUTO_INCREMENT,
                       username VARCHAR(125),
                       password VARCHAR(125),
                       fullname VARCHAR(125),
                       role VARCHAR(125),

                       PRIMARY KEY (Id),
                       UNIQUE KEY `UK_users_username` (`username`)
)
    ENGINE=InnoDB ;

/* Table Users */
insert into Users(fullname, username, password, role)
values("Administrator", "admin", "$2a$10$q94CTune54Cx0NzeLSuAJ.riUVmHL1K7ckNCnXOGmsTZI2Hn9pH0m", "ADMIN");
insert into Users(fullname, username, password, role)
values("User", "user", "$2a$10$9Ehdj.GBlKpydcstWooFqezfBXp.4HKYONh5I4otDw/UVK99LtIJy", "USER");

/* Table Trade */
insert into `trade` (`account`, `type`, `buyQuantity`, `benchmark`)
values ( 'toBeUpdated', 'Type1', 1, 'benchmark');
insert into `trade` (`account`, `type`, `buyQuantity`)
values ( 'toBeDeleted', 'Type2', 2);

/* Table CurvePoint */
insert into `curvepoint` (`curveId`, `term`, `value`, `asOfDate`)
values ( 10, 10.5, 10.6, '2012-12-12');
insert into `curvepoint` (`curveId`, `term`, `value`)
values ( 20, 20.5, 20.6);

/* Table Rating */
insert into `rating` (`moodysRating`, `sandPRating`, `fitchRating`, `orderNumber`)
values ( 'toBeUpdated', 'sand', 'fitch', 1);
insert into `rating` (`moodysRating`, `sandPRating`, `fitchRating`, `orderNumber`)
values ( 'toBeDeleted',  'sand', 'fitch', 2);

/* Table RuleName */
insert into `rulename` (`name`, `description`, `json`, `template`, `sqlStr`, `sqlPart`)
values ( 'toBeUpdated', 'description', 'json', 'template', 'sqlStr', 'sqlPart');
insert into `rulename` (`name`, `description`, `json`, `template`, `sqlStr`, `sqlPart`)
values ( 'toBeDeleted', 'description', 'json', 'template', 'sqlStr', 'sqlPart');

/* Table BidList */
insert into `bidlist` (`account`, `type`, `bidQuantity`, `benchmark`)
values ( 'toBeUpdated', 'Type', 1, 'benchmark');
insert into `bidlist` (`account`, `type`, `bidQuantity`)
values ( 'toBeDeleted', 'Type2', 2);


