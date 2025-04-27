drop database cinema;
CREATE database cinema;

use cinema;
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

-- Phim 1
('Mario Bros', 'Mỹ', 95, 'Aaron Horvath', 'Chris Pratt, Anya Taylor-Joy',
6, 'Ngừng chiếu', 'Mario và em trai Luigi là hai thợ sửa ống nước bình thường tại Brooklyn. Một ngày nọ, họ bị cuốn vào một đường ống bí ẩn dẫn đến Vương quốc Nấm, nơi Công chúa Peach bị bắt cóc bởi tên bạo chúa rùa Bowser. Trong hành trình giải cứu, Mario phải đối mặt với nhiều thử thách, hợp tác với những người bạn mới như Toad và Donkey Kong để ngăn chặn âm mưu thống trị của Bowser. Phim là sự kết hợp giữa hành động, hài hước và những hình ảnh rực rỡ mang lại trải nghiệm giải trí đầy màu sắc cho mọi lứa tuổi.',
'https://www.youtube.com/watch?v=TnGl01FkMMo', 'poster/marioAd.jpg', 'poster/mario', '2023-04-05')
,

-- Phim 2
('Rocky', 'Mỹ', 120, 'John G. Avildsen', 'Sylvester Stallone',
13, 'Ngừng chiếu', 'Rocky Balboa là một võ sĩ quyền anh vô danh đến từ Philadelphia. Khi nhà vô địch hạng nặng Apollo Creed quyết định trao cơ hội cho một võ sĩ nghiệp dư, Rocky được chọn để thách đấu. Dù không có gì trong tay ngoài tinh thần kiên cường và trái tim sắt đá, Rocky quyết tâm luyện tập chăm chỉ để chứng minh giá trị của bản thân. Bộ phim không chỉ là câu chuyện về thể thao, mà còn là hành trình vượt qua chính mình, mang đến thông điệp về niềm tin, nỗ lực và hy vọng vượt qua nghịch cảnh.',
'https://www.youtube.com/watch?v=3VUblDwa648', 'poster/rockyAd.jpg', 'poster/rocky', '1976-11-21'),


-- Phim 3
('Ròm', 'Việt Nam', 89, 'Trần Thanh Huy', 'Trần Anh Khoa',
16, 'Ngừng chiếu', 'Ròm là một cậu bé sống trong khu nhà trọ nghèo tại Sài Gòn, chuyên đi chơi số đề cho các cư dân với hy vọng kiếm được tiền để sinh tồn. Tuy còn nhỏ tuổi nhưng Ròm đã thấu hiểu những góc khuất của cuộc sống đô thị, chứng kiến sự tuyệt vọng, niềm hy vọng và khát khao đổi đời của người nghèo. Với nhịp phim nhanh, hình ảnh chân thực và giọng kể mạnh mẽ, bộ phim mang lại một góc nhìn hiện thực sâu sắc về xã hội Việt Nam hiện đại, đồng thời khắc họa sức sống mãnh liệt của những con người ở tầng đáy.',
'https://www.youtube.com/watch?v=8ReL8GJUXqY', 'poster/romAd.jpg', 'poster/rom', '2020-09-25'),

-- Phim 4
('The Lion King', 'Mỹ', 118, 'Jon Favreau', 'Donald Glover, Beyoncé',
6, 'Ngừng chiếu', 'Simba là con trai của vua sư tử Mufasa, người trị vì vùng đất Pride Rock. Sau cái chết bất ngờ của cha mình do âm mưu của người chú độc ác Scar, Simba trốn khỏi vương quốc và lớn lên trong sự dằn vặt. Dưới sự giúp đỡ của những người bạn mới như Timon và Pumbaa, Simba dần tìm lại chính mình và quyết định trở về để giành lại ngôi vương và khôi phục sự cân bằng. Bộ phim không chỉ là hành trình trưởng thành, mà còn truyền tải bài học sâu sắc về danh dự, trách nhiệm và tình cảm gia đình.',
'https://www.youtube.com/watch?v=4CbLXeGSDxg', 'poster/lionkingAd.jpg', 'poster/lionking', '2019-07-19'),

-- Phim 5
('Immaculate', 'Ý', 90, 'Michele Soavi', 'Asia Argento',
18, 'Đang chiếu', 'Immaculate là một bộ phim kinh dị đậm chất tôn giáo, xoay quanh một nữ tu trẻ bị cuốn vào hàng loạt sự kiện kỳ bí trong tu viện nơi cô vừa gia nhập. Khi bắt đầu phát hiện những hiện tượng siêu nhiên cùng những bí mật tăm tối bị chôn giấu, cô dần rơi vào vòng xoáy hoài nghi và ám ảnh. Bộ phim kết hợp giữa tâm linh và yếu tố kinh dị tâm lý, đặt ra câu hỏi về sự thuần khiết, đức tin và ranh giới giữa cái thiện và cái ác trong tâm hồn con người.',
'https://www.youtube.com/watch?v=5U1jujG7x1w', 'poster/immaculatteAd.jpg', 'poster/immaculatte', '2023-10-13'),

-- Phim 6
('How to Train Your Dragon: The Hidden World', 'Mỹ', 104, 'Dean DeBlois', 'Jay Baruchel, America Ferrera',
6, 'Ngừng chiếu', 'Trong phần cuối của loạt phim đình đám, Hiccup – giờ đây là thủ lĩnh của đảo Berk – tiếp tục nhiệm vụ tìm kiếm vùng đất huyền thoại “Thế giới Bí ẩn” để bảo vệ loài rồng khỏi nguy cơ tuyệt chủng. Trong khi đó, mối quan hệ giữa Răng Sún và một con rồng cái bí ẩn mang lại những bước ngoặt cảm động. Phim là hành trình của tình bạn, tình yêu và sự hy sinh, đan xen với những cảnh bay hoành tráng và đồ họa tuyệt đẹp, để lại dư âm sâu lắng cho khán giả mọi lứa tuổi.',
'https://www.youtube.com/watch?v=SkcucKDrbOI', 'poster/httydAd.jpg', 'poster/httyd', '2019-02-22')
,

-- Phim 7
('Ant-Man and the Wasp: Quantumania', 'Mỹ', 120, 'Peyton Reed', 'Paul Rudd, Evangeline Lilly',
13, 'Đang chiếu', 'Ant-Man Scott Lang cùng Hope Van Dyne và gia đình bị cuốn vào Thế giới Lượng tử, một vũ trụ kỳ bí nằm ngoài quy luật không gian – thời gian. Tại đây, họ gặp những sinh vật lạ và phải đối mặt với Kang – kẻ chinh phục có sức mạnh vượt trội. Phim mở rộng vũ trụ điện ảnh Marvel sang những chiều không gian mới, mang đến hành động mãn nhãn, những khoảnh khắc cảm động và mở đường cho các sự kiện hoành tráng sắp tới.',
'https://www.youtube.com/watch?v=ZlNFpri-Y40', 'poster/antmanAd.jpg', 'poster/antman', '2023-07-15'),

-- Phim 8
 ('Avengers: Infinity War', 'Mỹ', 149, 'Anthony and Joe Russo', 'Robert Downey Jr., Chris Evans',
13, 'Ngừng chiếu', 'Avengers: Infinity War là cuộc đối đầu đỉnh cao giữa biệt đội Avengers và Thanos – kẻ độc tài vũ trụ muốn xóa sổ một nửa sự sống bằng các Viên đá Vô cực. Các siêu anh hùng từ khắp nơi trong vũ trụ hợp sức để ngăn chặn âm mưu của hắn, nhưng cái giá phải trả vô cùng khốc liệt. Phim là bước ngoặt lớn của Vũ trụ điện ảnh Marvel với quy mô hoành tráng, cảm xúc mãnh liệt và những khoảnh khắc khiến khán giả nghẹt thở đến phút cuối.',
'https://www.youtube.com/watch?v=6ZfuNTqbHE8', 'poster/avengersAd.jpg', 'poster/avengers', '2018-04-27');
 use cinema;
 INSERT INTO Room (RowNumber, ColNumber, RoomStatus, Name)
VALUES
(5, 5, 'Hoạt động', 'Phòng 1'),
(8, 12, 'Hoạt động', 'Phòng 2'),
(6 , 10, 'Bảo trì', 'Phòng 3');
INSERT INTO SeatType (Name, Cost)
VALUES
('Thường', 70000),
('VIP', 100000);
INSERT INTO Seats (Position, RoomId, SeatTypeId)
VALUES
('A3', 1, 1), ('A4', 1, 1), ('A5', 1, 1),
('B3', 1, 2), ('B4', 1, 2), ('B5', 1, 2),
('A1', 2, 1), ('A2', 2, 1),
('B1', 2, 2), ('B2', 2, 2);
-- Phim 6: Immaculatte (Id = 6)
-- Phim 1: Mario Bros (FilmId = 1)
-- Phim 1: Mario Bros (FilmId = 1)
INSERT INTO MovieShow (StartTime, EndTime, FilmId, RoomId)
VALUES
('2025-04-26 16:00:00', '2025-04-26 17:30:00', 1, 2),
('2025-04-26 19:00:00', '2025-04-26 20:30:00', 1, 1),
('2025-04-27 10:00:00', '2025-04-27 11:30:00', 1, 1),
('2025-04-27 13:00:00', '2025-04-27 14:30:00', 1, 2);

-- Phim 2: Rocky (FilmId = 2)
INSERT INTO MovieShow (StartTime, EndTime, FilmId, RoomId)
VALUES
('2025-04-26 12:00:00', '2025-04-26 14:00:00', 2, 1),
('2025-04-26 15:00:00', '2025-04-26 17:00:00', 2, 2),
('2025-04-27 20:00:00', '2025-04-27 22:00:00', 2, 2),
('2025-04-27 22:30:00', '2025-04-28 00:30:00', 2, 1),
('2025-04-28 14:00:00', '2025-04-28 16:00:00', 2, 1);

-- Phim 3: Ròm (FilmId = 3)
INSERT INTO MovieShow (StartTime, EndTime, FilmId, RoomId)
VALUES
('2025-04-26 14:00:00', '2025-04-26 15:30:00', 3, 1),
('2025-04-26 16:00:00', '2025-04-26 17:30:00', 3, 2),
('2025-04-27 18:00:00', '2025-04-27 19:30:00', 3, 2),
('2025-04-27 20:00:00', '2025-04-27 21:30:00', 3, 1),
('2025-04-28 16:00:00', '2025-04-28 17:30:00', 3, 1);

-- Phim 4: The Lion King (FilmId = 4)
INSERT INTO MovieShow (StartTime, EndTime, FilmId, RoomId)
VALUES
('2025-04-26 18:00:00', '2025-04-26 20:00:00', 4, 1),
('2025-04-26 20:30:00', '2025-04-26 22:30:00', 4, 2),
('2025-04-27 12:00:00', '2025-04-27 14:00:00', 4, 2),
('2025-04-27 16:00:00', '2025-04-27 18:00:00', 4, 1),
('2025-04-28 20:00:00', '2025-04-28 22:00:00', 4, 1);

-- Phim 5: Immaculatte (FilmId = 5)
INSERT INTO MovieShow (StartTime, EndTime, FilmId, RoomId)
VALUES
('2025-04-26 14:00:00', '2025-04-26 15:30:00', 5, 1),
('2025-04-26 16:00:00', '2025-04-26 17:30:00', 5, 2),
('2025-04-27 18:00:00', '2025-04-27 19:30:00', 5, 2),
('2025-04-27 20:00:00', '2025-04-27 21:30:00', 5, 1),
('2025-04-28 16:00:00', '2025-04-28 17:30:00', 5, 1);

-- Phim 7: Ant-Man and the Wasp: Quantumania (FilmId = 7)
INSERT INTO MovieShow (StartTime, EndTime, FilmId, RoomId)
VALUES
('2025-04-26 20:00:00', '2025-04-26 22:00:00', 7, 1),
('2025-04-26 22:30:00', '2025-04-27 00:30:00', 7, 2),
('2025-04-27 14:00:00', '2025-04-27 16:00:00', 7, 2),
('2025-04-27 16:30:00', '2025-04-27 18:30:00', 7, 1),
('2025-04-28 18:00:00', '2025-04-28 20:00:00', 7, 1);

-- Phim 8: Avengers: Infinity War (FilmId = 8)
INSERT INTO MovieShow (StartTime, EndTime, FilmId, RoomId)
VALUES
('2025-04-26 12:00:00', '2025-04-26 14:00:00', 8, 1),
('2025-04-26 14:30:00', '2025-04-26 16:30:00', 8, 2),
('2025-04-27 20:00:00', '2025-04-27 22:00:00', 8, 2),
('2025-04-27 22:30:00', '2025-04-28 00:30:00', 8, 1),
('2025-04-28 14:00:00', '2025-04-28 16:00:00', 8, 1);


INSERT INTO Genre (Name)
VALUES
('Hành động'),
('Hoạt hình'),
('Kinh dị'),
('Hài'),
('Tâm lý');
-- Immaculatte (FilmId = 6) là Kinh dị
INSERT INTO Film_Genre (FilmId, GenreId)
VALUES (6, 3);

-- Ant-Main (FilmId = 8) là Hành động + Hài
INSERT INTO Film_Genre (FilmId, GenreId)
VALUES
(7, 1),
(7, 4);
INSERT INTO Film_Genre (FilmId, GenreId)
VALUES (4, 5);

-- Mario Bros (FilmId = 2): Hoạt hình + Hài
INSERT INTO Film_Genre (FilmId, GenreId)
VALUES (1, 2), (1, 4);

-- Rocky (FilmId = 3): Hành động + Tâm lý
INSERT INTO Film_Genre (FilmId, GenreId)
VALUES (2, 1), (2, 5);

-- Avengers (FilmId = 9): Hành động
INSERT INTO Film_Genre (FilmId, GenreId)
VALUES (8, 1);



INSERT INTO Food (Name, Description, Cost) VALUES
('Pizza', 'Pizza hải sản đặc biệt với phô mai ngon tuyệt.', 100000),
('Burger', 'Burger thịt bò, rau xà lách, phô mai.', 50000),
('Hotdog', 'Hotdog với xúc xích và sốt đặc biệt.', 30000),
('Coca Cola', 'Nước giải khát Coca Cola.', 20000),
('Pepsi', 'Một lon Pepsi mát lạnh.', 20000),
('Sprite', 'Nước giải khát Sprite vị chanh tươi.', 20000),
('Fanta', 'Nước ngọt Fanta vị cam đặc biệt.', 20000),
('7Up', '7Up nước giải khát vị chanh tự nhiên.', 22000);
INSERT INTO User (Name, Gender, Birth, Phone, Address, AccountId) VALUES
('Quốc Thuận', 1, '1990-01-01', '0123456789', 'Hà Nội', 1),
('Nguyễn Văn A', 1, '1992-05-15', '0987654321', 'Hồ Chí Minh', 2),
('Trần Thị B', 0, '1995-08-20', '0934567890', 'Đà Nẵng', 3);

INSERT INTO Bill (UserId, DatePurchased, BillStatus) VALUES
(1, '2025-04-27 10:18:19', 'Paid'),  -- Hóa đơn 1
(2, '2025-04-27 10:20:10', 'Paid'),  -- Hóa đơn 2
(3, '2025-04-27 10:22:00', 'Paid');  -- Hóa đơn 3
INSERT INTO Food_Order (FoodId, BillId, Count) VALUES
(1, 1, 2),  -- 2 cái Pizza trong hóa đơn 1
(2, 1, 3),  -- 3 cái Burger trong hóa đơn 1
(3, 1, 1),  -- 1 cái Hotdog trong hóa đơn 1
(4, 2, 2),  -- 2 lon Coca Cola trong hóa đơn 2
(5, 2, 3),  -- 3 lon Pepsi trong hóa đơn 2
(6, 2, 1),  -- 1 lon Sprite trong hóa đơn 2
(7, 3, 1),  -- 1 lon Fanta trong hóa đơn 3
(8, 3, 2);  -- 2 lon 7Up trong hóa đơn 3




