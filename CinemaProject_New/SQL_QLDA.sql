create database QLDA
use QLDA
drop database QLDA
CREATE TABLE khoa (
    ma_khoa VARCHAR(10) PRIMARY KEY,
    ten_khoa VARCHAR(100)
);

CREATE TABLE bo_mon (
    ma_bomon VARCHAR(10) PRIMARY KEY,
    ten_bomon VARCHAR(100),
    ma_khoa VARCHAR(10),
    FOREIGN KEY (ma_khoa) REFERENCES khoa(ma_khoa)
);

CREATE TABLE giang_vien (
    ma_gv VARCHAR(10) PRIMARY KEY,
    ho_ten VARCHAR(100),
    sdt VARCHAR(20),
    email VARCHAR(100),
    ma_bomon VARCHAR(10),
    FOREIGN KEY (ma_bomon) REFERENCES bo_mon(ma_bomon)
);

CREATE TABLE sinh_vien (
    ma_sv VARCHAR(10) PRIMARY KEY,
    ho_ten VARCHAR(100),
    sdt VARCHAR(20),
    email VARCHAR(100),
    lop VARCHAR(20),
    ma_khoa VARCHAR(10),
    FOREIGN KEY (ma_khoa) REFERENCES khoa(ma_khoa)
);

CREATE TABLE loai_doan (
    ma_loai VARCHAR(10) PRIMARY KEY,
    ten_loai VARCHAR(100)
);

CREATE TABLE do_an (
    ma_doan VARCHAR(10) PRIMARY KEY,
    ten_doan VARCHAR(100),
    mo_ta TEXT,
    ngay_baocao DATE,
    trang_thai VARCHAR(20),
    ma_loai VARCHAR(10),
    FOREIGN KEY (ma_loai) REFERENCES loai_doan(ma_loai)
);

CREATE TABLE phan_cong (
    ma_sv VARCHAR(10),
    ma_gv VARCHAR(10),
    ngay_phancong DATE,
    vai_tro VARCHAR(50),
    trang_thai VARCHAR(20),
    PRIMARY KEY (ma_sv, ma_gv),
    FOREIGN KEY (ma_sv) REFERENCES sinh_vien(ma_sv),
    FOREIGN KEY (ma_gv) REFERENCES giang_vien(ma_gv)
);

CREATE TABLE lam (
    ma_sv VARCHAR(10),
    ma_doan VARCHAR(10),
    PRIMARY KEY (ma_sv, ma_doan),
    FOREIGN KEY (ma_sv) REFERENCES sinh_vien(ma_sv),
    FOREIGN KEY (ma_doan) REFERENCES do_an(ma_doan)
);

CREATE TABLE huong_dan (
    ma_gv VARCHAR(10),
    ma_doan VARCHAR(10),
    PRIMARY KEY (ma_gv, ma_doan),
    FOREIGN KEY (ma_gv) REFERENCES giang_vien(ma_gv),
    FOREIGN KEY (ma_doan) REFERENCES do_an(ma_doan)
);

-- 1. Khoa
INSERT INTO khoa VALUES 
('K01', 'Công nghệ thông tin'),
('K02', 'Kỹ thuật điện'),
('K03', 'Quản trị kinh doanh'),
('K04', 'Cơ khí chế tạo'),
('K05', 'Môi trường'),
('K06', 'Kỹ thuật ô tô'),
('K07', 'Điện tử - Viễn thông'),
('K08', 'Kinh tế'),
('K09', 'Ngôn ngữ Anh'),
('K10', 'Sư phạm');

-- 2. Bộ môn
INSERT INTO bo_mon VALUES 
('BM01', 'Khoa học máy tính', 'K01'),
('BM02', 'Hệ thống thông tin', 'K01'),
('BM03', 'Điện tử', 'K02'),
('BM04', 'Cơ điện tử', 'K04'),
('BM05', 'Tài chính', 'K08'),
('BM06', 'Mạng máy tính', 'K01'),
('BM07', 'Ngữ pháp tiếng Anh', 'K09'),
('BM08', 'Giảng dạy Tin học', 'K10'),
('BM09', 'Kiểm toán', 'K08'),
('BM10', 'Kỹ thuật điều khiển', 'K06');

-- 3. Giảng viên
INSERT INTO giang_vien VALUES 
('GV01', 'Nguyễn Văn A', '0909123456', 'a.nguyen@univ.edu', 'BM01'),
('GV02', 'Trần Thị B', '0909222333', 'b.tran@univ.edu', 'BM02'),
('GV03', 'Lê Văn C', '0909333444', 'c.le@univ.edu', 'BM03'),
('GV04', 'Trần Thị A', '0909266333', 'a.tran@univ.edu', 'BM02'),
('GV05', 'Nguyễn Thị D', '0909444555', 'd.nguyen@univ.edu', 'BM05'),
('GV06', 'Phạm Văn E', '0909555666', 'e.pham@univ.edu', 'BM04'),
('GV07', 'Đỗ Thị F', '0909666777', 'f.do@univ.edu', 'BM06'),
('GV08', 'Trần Văn G', '0909777888', 'g.tran@univ.edu', 'BM01'),
('GV09', 'Nguyễn Thị H', '0909888999', 'h.nguyen@univ.edu', 'BM07'),
('GV10', 'Lê Thị I', '0909999000', 'i.le@univ.edu', 'BM08');

-- 4. Sinh viên
INSERT INTO sinh_vien VALUES 
('SV01', 'Phạm Văn D', '0911111222', 'd.pham@student.edu', 'D20CNTT01', 'K01'),
('SV02', 'Ngô Thị E', '0911222333', 'e.ngo@student.edu', 'D20CNTT02', 'K01'),
('SV03', 'Hoàng Văn F', '0911333444', 'f.hoang@student.edu', 'D20D01', 'K03'),
('SV04', 'Vũ Minh G', '0911444555', 'g.vu@student.edu', 'D20DT01', 'K02'),
('SV05', 'Lê Văn H', '0911555666', 'h.le@student.edu', 'D20CK01', 'K04'),
('SV06', 'Phạm Thị I', '0911666777', 'i.pham@student.edu', 'D20M01', 'K05'),
('SV07', 'Nguyễn Văn J', '0911777888', 'j.nguyen@student.edu', 'D20KT01', 'K08'),
('SV08', 'Trần Thị K', '0911888999', 'k.tran@student.edu', 'D20TA01', 'K09'),
('SV09', 'Đặng Thị L', '0911999000', 'l.dang@student.edu', 'D20SP01', 'K10'),
('SV10', 'Mai Văn M', '0911000111', 'm.mai@student.edu', 'D20O01', 'K06');

-- 5. Loại đồ án
INSERT INTO loai_doan VALUES 
('L01', 'Đồ án chuyên ngành'),
('L02', 'Đồ án tốt nghiệp'),
('L03', 'Đồ án thực tập'),
('L04', 'Đề tài nghiên cứu'),
('L05', 'Khóa luận đại học'),
('L06', 'Chuyên đề tự chọn'),
('L07', 'Dự án cộng đồng'),
('L08', 'Báo cáo seminar'),
('L09', 'Đề án khởi nghiệp'),
('L10', 'Đồ án song ngữ');

-- 6. Đồ án
INSERT INTO do_an VALUES 
('DA01', 'Hệ thống quản lý sinh viên', 'Ứng dụng quản lý sinh viên đại học', '2025-06-01', 'Đang thực hiện', 'L01'),
('DA02', 'Thiết kế mạch điện thông minh', 'Ứng dụng IoT trong điện dân dụng', '2025-06-15', 'Hoàn thành', 'L02'),
('DA03', 'Quản lý nhà trọ', 'Ứng dụng Android quản lý phòng trọ', '2025-06-10', 'Đang thực hiện', 'L01'),
('DA04', 'Web bán sách online', 'Xây dựng web bằng PHP', '2025-05-30', 'Hoàn thành', 'L02'),
('DA05', 'Phân tích dữ liệu mạng xã hội', 'Sử dụng Python và ML', '2025-05-28', 'Đang thực hiện', 'L04'),
('DA06', 'Thiết kế robot line follower', 'Arduino và cảm biến', '2025-06-05', 'Đang thực hiện', 'L03'),
('DA07', 'Ứng dụng đặt vé xe', 'Ứng dụng Java Spring Boot', '2025-06-07', 'Đã nghiệm thu', 'L01'),
('DA08', 'Hệ thống đánh giá giảng viên', 'PHP Laravel + MySQL', '2025-05-25', 'Hoàn thành', 'L05'),
('DA09', 'Dự án trồng rau sạch', 'IoT và hệ thống tưới tự động', '2025-06-12', 'Đang thực hiện', 'L07'),
('DA10', 'Nền tảng học online', 'E-learning platform', '2025-06-20', 'Đang thực hiện', 'L10');

-- 7. Phân công
INSERT INTO phan_cong VALUES 
('SV01', 'GV01', '2025-03-01', 'Hướng dẫn chính', 'Đang hướng dẫn'),
('SV02', 'GV02', '2025-03-02', 'Đồng hướng dẫn', 'Hoàn thành'),
('SV03', 'GV03', '2025-03-03', 'Phản biện', 'Đã đánh giá'),
('SV04', 'GV04', '2025-03-04', 'Hướng dẫn chính', 'Đang hướng dẫn'),
('SV05', 'GV05', '2025-03-05', 'Phản biện', 'Đang đánh giá'),
('SV06', 'GV06', '2025-03-06', 'Hướng dẫn chính', 'Đã đánh giá'),
('SV07', 'GV07', '2025-03-07', 'Hướng dẫn chính', 'Đang hướng dẫn'),
('SV08', 'GV08', '2025-03-08', 'Hướng dẫn chính', 'Hoàn thành'),
('SV09', 'GV09', '2025-03-09', 'Phản biện', 'Đang đánh giá'),
('SV10', 'GV10', '2025-03-10', 'Hướng dẫn chính', 'Đang hướng dẫn');

-- 8. Làm
INSERT INTO lam VALUES 
('SV01', 'DA01'),
('SV02', 'DA01'),
('SV03', 'DA02'),
('SV04', 'DA03'),
('SV05', 'DA04'),
('SV06', 'DA05'),
('SV07', 'DA06'),
('SV08', 'DA07'),
('SV09', 'DA08'),
('SV10', 'DA09');

-- 9. Hướng dẫn
INSERT INTO huong_dan VALUES 
('GV01', 'DA01'),
('GV02', 'DA01'),
('GV03', 'DA02'),
('GV04', 'DA03'),
('GV05', 'DA04'),
('GV06', 'DA05'),
('GV07', 'DA06'),
('GV08', 'DA07'),
('GV09', 'DA08'),
('GV10', 'DA09');


-- 1
SELECT 
    sv.ma_sv, sv.ho_ten AS ten_sinh_vien, da.ten_doan, gv.ho_ten AS ten_giang_vien, pc.vai_tro
FROM 
    sinh_vien sv
JOIN lam l ON sv.ma_sv = l.ma_sv
JOIN do_an da ON l.ma_doan = da.ma_doan
JOIN phan_cong pc ON sv.ma_sv = pc.ma_sv
JOIN giang_vien gv ON pc.ma_gv = gv.ma_gv
WHERE pc.vai_tro LIKE '%hướng dẫn chính%';

-- 2
SELECT 
    ld.ten_loai, COUNT(l.ma_sv) AS so_sinh_vien
FROM 
    loai_doan ld
JOIN do_an da ON ld.ma_loai = da.ma_loai
JOIN lam l ON da.ma_doan = l.ma_doan
GROUP BY ld.ten_loai;

-- 3
SELECT 
    gv.ma_gv, gv.ho_ten
FROM 
    giang_vien gv
LEFT JOIN huong_dan hd ON gv.ma_gv = hd.ma_gv
WHERE 
    hd.ma_gv IS NULL;
-- 4
SELECT 
    sv.ma_sv, sv.ho_ten, da.ten_doan, da.trang_thai
FROM 
    sinh_vien sv
JOIN lam l ON sv.ma_sv = l.ma_sv
JOIN do_an da ON l.ma_doan = da.ma_doan
JOIN khoa k ON sv.ma_khoa = k.ma_khoa
WHERE 
    k.ten_khoa = 'Công nghệ thông tin'
    AND da.trang_thai = 'Đang thực hiện';
    
-- 5 giảng viên hướng dẫn nhiều sv nhất
SELECT 
    gv.ma_gv, 
    gv.ho_ten, 
    COUNT(DISTINCT pc.ma_sv) AS so_sinh_vien
FROM 
    giang_vien gv
JOIN phan_cong pc ON gv.ma_gv = pc.ma_gv
WHERE 
    pc.vai_tro LIKE '%hướng dẫn chính%'
GROUP BY gv.ma_gv, gv.ho_ten
ORDER BY so_sinh_vien DESC;

