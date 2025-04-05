CREATE database cinema

use cinema
CREATE TABLE Role (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Name NVARCHAR(255)
);

CREATE TABLE Account (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Email NVARCHAR(255) NOT NULL,
    Password NVARCHAR(255) NOT NULL,
    AccountStatus NVARCHAR(50),
    RoleId INT,
    FOREIGN KEY (RoleId) REFERENCES Role(Id)
);

CREATE TABLE User (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Name NVARCHAR(255),
    Gender BIT,
    Birth DATE,
    Phone NVARCHAR(20),
    Address NVARCHAR(255),
    AccountId INT,
    FOREIGN KEY (AccountId) REFERENCES Account(Id)
);

