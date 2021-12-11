/* Setting up PROD DB */
create database P4_prod;
use P4_prod;

create table parking(
PARKING_NUMBER int PRIMARY KEY,
AVAILABLE bool NOT NULL,
TYPE varchar(10) NOT NULL
);

create table ticket(
 ID int PRIMARY KEY AUTO_INCREMENT,
 PARKING_NUMBER int NOT NULL,
 VEHICLE_REG_NUMBER varchar(10) NOT NULL,
 PRICE double,
 IN_TIME DATETIME NOT NULL,
 OUT_TIME DATETIME,
 FOREIGN KEY (PARKING_NUMBER)
 REFERENCES parking(PARKING_NUMBER));

insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(1,true,'CAR');
insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(2,true,'CAR');
insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(3,true,'CAR');
insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(4,true,'BIKE');
insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(5,true,'BIKE');
commit;

/* Setting up TEST DB */
create database P4_test;
use P4_test;

create table parking(
PARKING_NUMBER int PRIMARY KEY,
AVAILABLE bool NOT NULL,
TYPE varchar(10) NOT NULL
);

create table ticket(
 ID int PRIMARY KEY AUTO_INCREMENT,
 PARKING_NUMBER int NOT NULL,
 VEHICLE_REG_NUMBER varchar(10) NOT NULL,
 PRICE double,
 IN_TIME DATETIME NOT NULL,
 OUT_TIME DATETIME,
 FOREIGN KEY (PARKING_NUMBER)
 REFERENCES parking(PARKING_NUMBER));

insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(1,true,'CAR');
insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(2,true,'CAR');
insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(3,true,'CAR');
insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(4,true,'BIKE');
insert into parking(PARKING_NUMBER,AVAILABLE,TYPE) values(5,true,'BIKE');
commit;
