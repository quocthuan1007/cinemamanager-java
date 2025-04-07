CREATE database cinema;

use cinema;
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
INSERT INTO Role (Name) VALUES
('Admin'),
('Staff'),
('Customer');

INSERT INTO Account (Email, Password, AccountStatus, RoleId)
VALUES
('quocthuan@gmail.com', '123123123', 'ONLINE', 1),
('staff@gmail.com', 'staff123', 'ONLINE', 2),
('customer@gmail.com', 'cust123', 'OFFLINE', 3);



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

CREATE TABLE Genre (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Name NVARCHAR(255) NOT NULL
);

CREATE TABLE Film (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Name NVARCHAR(255) NOT NULL,
    Country NVARCHAR(255),
    Length INT,
    Director NVARCHAR(255),
    Actor NVARCHAR(255),
    AgeLimit INT,
    FilmStatus NVARCHAR(50),
    Content TEXT,  -- Sửa từ NVARCHAR(MAX)
    Trailer TEXT,  -- Sửa từ NVARCHAR(MAX)
    AdPosterUrl TEXT, -- Sửa từ NVARCHAR(MAX)
    PosterUrl TEXT, -- Sửa từ NVARCHAR(MAX)
    ReleaseDate DATETIME(2)
);

CREATE TABLE Film_Genre (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    FilmId INT,
    GenreId INT,
    FOREIGN KEY (FilmId) REFERENCES Film(Id) ON DELETE CASCADE,
    FOREIGN KEY (GenreId) REFERENCES Genre(Id) ON DELETE CASCADE
);

CREATE TABLE Room (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    RowNumber INT,
    ColNumber INT,
    RoomStatus NVARCHAR(50),
    Name NVARCHAR(255)
);

CREATE TABLE MovieShow  (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    StartTime DATETIME(2),
    EndTime DATETIME(2),
    FilmId INT,
    RoomId INT,
    IsDeleted BIT DEFAULT 0,
    FOREIGN KEY (FilmId) REFERENCES Film(Id) ON DELETE CASCADE,
    FOREIGN KEY (RoomId) REFERENCES Room(Id) ON DELETE CASCADE
);

CREATE TABLE SeatType (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Name NVARCHAR(255),
    Cost INT
);

CREATE TABLE Seats (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Position NVARCHAR(255),
    RoomId INT,
    SeatTypeId INT,
    FOREIGN KEY (RoomId) REFERENCES Room(Id) ON DELETE CASCADE,
    FOREIGN KEY (SeatTypeId) REFERENCES SeatType(Id) ON DELETE CASCADE
);

CREATE TABLE Bill (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    UserId INT,
    DatePurchased DATETIME(2),
    BillStatus NVARCHAR(50),
    FOREIGN KEY (UserId) REFERENCES User(Id) ON DELETE CASCADE
);

CREATE TABLE Reservation (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    BillId INT,
    SeatId INT,
    ShowId INT,
    Cost INT,
    SeatTypeName TEXT, -- Sửa từ NVARCHAR(longtext)
    FOREIGN KEY (BillId) REFERENCES Bill(Id) ON DELETE CASCADE,
    FOREIGN KEY (SeatId) REFERENCES Seats(Id) ON DELETE CASCADE,
    FOREIGN KEY (ShowId) REFERENCES MovieShow (Id) ON DELETE CASCADE
);

CREATE TABLE Food (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Name NVARCHAR(255) NOT NULL,
    Description TEXT, -- Sửa từ NVARCHAR(MAX)
    Cost INT
);

CREATE TABLE Food_Order (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    FoodId INT,
    BillId INT,
    Count INT,
    FOREIGN KEY (FoodId) REFERENCES Food(Id) ON DELETE CASCADE,
    FOREIGN KEY (BillId) REFERENCES Bill(Id) ON DELETE CASCADE
);

INSERT INTO Film (Name, Country, Length, Director, Actor, AgeLimit, FilmStatus, Content, Trailer, AdPosterUrl, PosterUrl, ReleaseDate)
VALUES

-- Phim 2
('Mario Bros', 'Mỹ', 95, 'Aaron Horvath', 'Chris Pratt, Anya Taylor-Joy',
 6, 'Ngừng chiếu', 'Anh em Mario giải cứu Vương quốc Nấm khỏi Bowser.',
 'https://www.youtube.com/watch?v=TnGl01FkMMo', 'poster/marioAd.jpg', 'poster/mario', '2023-04-05'),

-- Phim 3
('Rocky', 'Mỹ', 120, 'John G. Avildsen', 'Sylvester Stallone',
 13, 'Ngừng chiếu', 'Hành trình của võ sĩ quyền anh Rocky Balboa.',
 'https://www.youtube.com/watch?v=3VUblDwa648', 'poster/rockyAd.jpg', 'poster/rocky', '1976-11-21'),

-- Phim 4
('Ròm', 'Việt Nam', 89, 'Trần Thanh Huy', 'Trần Anh Khoa',
 16, 'Ngừng chiếu', 'Câu chuyện về cậu bé sống ở Sài Gòn và kiếm tiền bằng cách chơi số đề.',
 'https://www.youtube.com/watch?v=8ReL8GJUXqY', 'poster/romAd.jpg', 'poster/rom', '2020-09-25'),

-- Phim 5
('The Lion King', 'Mỹ', 118, 'Jon Favreau', 'Donald Glover, Beyoncé',
 6, 'Ngừng chiếu', 'Hành trình trưởng thành của sư tử Simba.',
 'https://www.youtube.com/watch?v=7TavVZMewpY', 'poster/lionkingAd.jpg', 'poster/lionking', '2019-07-19'),

-- Phim 6
('Immaculatte', 'Ý', 90, 'Michele Soavi', 'Asia Argento',
 18, 'Đang chiếu', 'Một bộ phim kinh dị về sự thuần khiết và bóng tối nội tâm.',
 'https://www.youtube.com/watch?v=xyz123', 'poster/immaculatteAd.jpg', 'poster/immaculatte', '2023-10-13'),

-- Phim 7
('How to Train Your Dragon: The Hidden World', 'Mỹ', 104, 'Dean DeBlois', 'Jay Baruchel, America Ferrera',
 6, 'Ngừng chiếu', 'Hành trình cuối cùng giữa Hiccup và Rồng Răng Sún.',
 'https://www.youtube.com/watch?v=SkcucKDrbOI', 'poster/httydAd.jpg', 'poster/httyd', '2019-02-22'),

-- Phim 8
('Ant-Main', 'Mỹ', 120, 'Peyton Reed', 'Paul Rudd, Evangeline Lilly',
 13, 'Đang chiếu', 'Siêu anh hùng Ant-Man cùng với Wasp trong một nhiệm vụ mới.',
 'https://www.youtube.com/watch?v=ZlNFpri-Y40', 'poster/antmanAd.jpg', 'poster/antman', '2023-07-15'),

-- Phim 9
('Avengers: Infinity War', 'Mỹ', 149, 'Anthony and Joe Russo', 'Robert Downey Jr., Chris Evans',
 13, 'Ngừng chiếu', 'Biệt đội Avengers đối đầu với Thanos để cứu vũ trụ.',
 'https://www.youtube.com/watch?v=6ZfuNTqbHE8', 'poster/avengersAd.jpg', 'poster/avengers', '2018-04-27');
