-- -------------------------------
-- 1. Roles
-- -------------------------------
INSERT INTO roles (role_id, role_name, description)
VALUES (1, 'ROLE_ADMIN', 'Quản trị hệ thống'),
       (2, 'ROLE_PATIENT', 'Bệnh nhân'),
       (3, 'ROLE_DOCTOR', 'Bác sĩ'),
       (4, 'ROLE_RECEPTIONIST', 'Nhân viên tiếp đón'),
       (5, 'ROLE_LAB_STAFF', 'Nhân viên xét nghiệm'),
       (6, 'ROLE_CONSULTANT', 'Nhân viên tư vấn'),
       (7, 'ROLE_CASHIER', 'Nhân viên thu ngân')
ON DUPLICATE KEY UPDATE role_name = role_name;

INSERT IGNORE INTO permissions (name, description)
VALUES ('MANAGE_USERS', 'Quản lý tài khoản người dùng'),
       ('MANAGE_ROLES', 'Quản lý vai trò'),
       ('VIEW_REPORTS', 'Xem báo cáo'),
       ('CREATE_APPOINTMENT', 'Tạo lịch hẹn'),
       ('UPDATE_APPOINTMENT', 'Cập nhật lịch hẹn'),
       ('DELETE_APPOINTMENT', 'Xóa lịch hẹn'),
       ('VIEW_LAB_RESULTS', 'Xem kết quả xét nghiệm');


-- ROLE_ADMIN: tất cả quyền
INSERT IGNORE INTO role_permissions (role_id, permission_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7);

-- ROLE_PATIENT: chỉ xem báo cáo
INSERT IGNORE INTO role_permissions (role_id, permission_id)
VALUES (2, 3);

-- ROLE_DOCTOR: xem báo cáo, xem kết quả xét nghiệm
INSERT IGNORE INTO role_permissions (role_id, permission_id)
VALUES (3, 3),
       (3, 7);

-- ROLE_RECEPTIONIST: tạo/cập nhật/xóa lịch hẹn
INSERT IGNORE INTO role_permissions (role_id, permission_id)
VALUES (4, 4),
       (4, 5),
       (4, 6);

-- ROLE_LAB_STAFF: xem kết quả xét nghiệm
INSERT IGNORE INTO role_permissions (role_id, permission_id)
VALUES (5, 7);

-- ROLE_CONSULTANT: tạo/cập nhật lịch hẹn, xem báo cáo
INSERT IGNORE INTO role_permissions (role_id, permission_id)
VALUES (6, 3),
       (6, 4),
       (6, 5);

-- ROLE_CASHIER: xem báo cáo
INSERT IGNORE INTO role_permissions (role_id, permission_id)
VALUES (7, 3);

INSERT INTO product (id, name, description, price)
VALUES (1, 'La Roche-Posay Effaclar Foaming Gel',
        'Sữa rửa mặt dành cho da nhạy cảm, giúp làm sạch sâu, giảm dầu thừa và hỗ trợ giảm mụn cho da dầu nhạy cảm',
        250000),
       (2, 'Kem dưỡng ẩm chuyên sâu', 'Cung cấp độ ẩm mạnh mẽ, phục hồi hàng rào bảo vệ da sau điều trị.', 450000),
       (3, 'Serum Vitamin C chống lão hóa', 'Làm sáng da, mờ thâm nám và chống oxy hóa hiệu quả.', 650000),
       (4, 'Kem chống nắng SPF 50+', 'Bảo vệ da khỏi tia UVA/UVB, ngăn ngừa lão hóa sớm và sạm da.', 320000),
       (5, 'Mặt nạ đất sét thải độc', 'Giúp làm sạch lỗ chân lông, kiềm dầu và giảm mụn đầu đen.', 180000),
       (6, 'Liệu trình peel da hóa học', 'Sản phẩm dùng trong liệu trình peel da tại phòng khám để tái tạo da.',
        1200000),
       (7, 'Gel bí đao rửa mặt Cocoon',
        'Sữa rửa mặt tốt nhất thuần chay dành cho da dầu, da hỗn hợp thiên dầu và da mụn giúp làm sạch sâu dầu thừa, bụi bẩn và hỗ trợ giảm mụn ẩn, mụn viêm mà không gây khô da',
        163000),
       (8, 'Acnes Oil Control Cleanser - Sữa rửa mặt sạch nhờn',
        'Làm sạch sâu, điều tiết dầu tự nhiên, se lỗ chân lông và ngăn ngừa mụn hiệu quả', 56000),
       (9, 'Viên Uống Glutasi Glutathione Hỗ Trợ Chống Oxy Hóa (Hộp 60 Viên)',
        'Glutasi Glutathione giúp làm đẹp da, chống lão hóa. Hỗ trợ chống oxy hóa, giúp tăng cường chức năng thải độc',
        790000),
       (10, 'Ultra Hair Plus (30 viên) - Bổ sung vitamin và khoáng chất cho tóc chắc khỏe',
        ' Giúp nuôi dưỡng và chăm sóc mái tóc khỏe mạnh, suôn mượt, ngăn chặn tóc bạc sớm, hói đầu.', 920000),
       (11, 'Yukiwa - Hỗ trợ giữ ẩm và tăng độ đàn hồi cho da',
        'Sản phẩm cung cấp thành phần chống oxy hóa, hạn chế quá trình lão hóa, giúp làn da mềm mịn, sáng đẹp và tăng cường độ ẩm cho da',
        1180000);

INSERT INTO accounts (full_name, phone_number, email, password, date_of_birth, address, gender, avt_path, status, created_at, updated_at)
VALUES ('Nguyễn Văn Nam', '0912345678', 'nam.nguyen@gmail.com', '12345678', '1988-02-25',
        '123 Phố Tràng Tiền, Quận Hoàn Kiếm, Hà Nội', 'Male', NULL, 'Active', '2024-05-10 10:00:00',
        '2025-07-20 15:30:00'),
       ('Phạm Thị Thu', '0987654321', 'thu.pham@gmail.com', '12345678', '1995-07-10',
        '456 Phố Bà Triệu, Quận Hai Bà Trưng, Hà Nội', 'Female', NULL, 'Active', '2024-06-15 11:30:00',
        '2025-08-01 09:45:00'),
       ('Lê Hoàng Long', '0901122334', 'long.le@gmail.com', '12345678', '2000-11-20',
        '789 Đường Láng, Quận Đống Đa, Hà Nội', 'Male', NULL, 'Active', '2024-07-20 14:00:00', '2025-08-25 10:10:00'),
       ('Trần Thanh Mai', '0934567890', 'maitran@gmail.com', '12345678', '1992-04-12',
        '101 Phố Hàng Bông, Quận Hoàn Kiếm, Hà Nội', 'Female', NULL, 'Active', '2024-08-01 09:20:00',
        '2025-06-12 17:05:00'),
       ('Võ Văn Tuấn', '0967890123', 'tuanvo@gmail.com', '12345678', '1985-09-30',
        '202 Đường Xuân Thủy, Quận Cầu Giấy, Hà Nội', 'Male', NULL, 'Active', '2024-09-05 16:45:00',
        '2025-05-30 11:20:00'),
       ('Hoàng Thị Lan', '0978901234', 'lan.hoang@gmail.com', '12345678', '1975-06-05',
        '303 Đường Kim Mã, Quận Ba Đình, Hà Nội', 'Female', NULL, 'Active', '2024-10-20 08:30:00',
        '2025-04-15 14:00:00'),
       ('Đinh Quang Minh', '0908765432', 'minhdinh@gmail.com', '12345678', '1998-01-18',
        '404 Phố Khâm Thiên, Quận Đống Đa, Hà Nội', 'Male', NULL, 'Active', '2024-11-25 12:15:00',
        '2025-03-22 09:50:00'),
       ('Nguyễn Thị Trâm', '0919283746', 'tramnguyen@gmail.com', '12345678', '1991-03-22',
        '505 Đường Nguyễn Trãi, Quận Thanh Xuân, Hà Nội', 'Female', NULL, 'Active', '2025-01-05 10:40:00',
        '2025-02-10 16:30:00'),
       ('Bùi Văn Thành', '0945678901', 'thanhbui@gmail.com', '12345678', '1980-08-01',
        '606 Đường Quang Trung, Quận Gò Vấp, Hà Nội', 'Male', NULL, 'Active', '2025-02-10 15:00:00',
        '2025-03-01 10:00:00'),
       ('Phạm Lan Hương', '0923456789', 'huongpham@gmail.com', '12345678', '1989-10-10',
        '707 Phố Lê Văn Lương, Quận Thanh Xuân, Hà Nội', 'Female', NULL, 'Active', '2025-03-15 09:00:00',
        '2025-04-05 11:00:00');

INSERT INTO accounts (full_name, phone_number, email, password, date_of_birth, address, gender, avt_path, status, created_at, updated_at)
VALUES
    ('Nguyễn Văn Hậu', '0911111222', 'hau.nguyen@gmail.com', '12345678', '1993-12-15',
     '808 Đường Nguyễn Chí Thanh, Quận Đống Đa, Hà Nội', 'Male', NULL, 'Active', '2025-03-20 10:00:00', '2025-08-10 12:00:00'),
    ('Trần Thị Hồng', '0933333444', 'hong.tran@gmail.com', '12345678', '1996-09-09',
     '909 Đường Cầu Giấy, Quận Cầu Giấy, Hà Nội', 'Female', NULL, 'Active', '2025-03-25 11:00:00', '2025-08-12 13:30:00'),
    ('Phạm Văn Dũng', '0944444555', 'dung.pham@gmail.com', '12345678', '1982-02-28',
     '111 Phố Tây Sơn, Quận Đống Đa, Hà Nội', 'Male', NULL, 'Active', '2025-04-01 09:45:00', '2025-08-14 14:20:00'),
    ('Lê Thị Ngọc', '0955555666', 'ngoc.le@gmail.com', '12345678', '1990-05-18',
     '222 Đường Nguyễn Văn Cừ, Quận Long Biên, Hà Nội', 'Female', NULL, 'Active', '2025-04-10 08:30:00', '2025-08-16 15:10:00'),
    ('Hoàng Văn Hải', '0966666777', 'hai.hoang@gmail.com', '12345678', '1987-07-25',
     '333 Đường Phạm Hùng, Quận Nam Từ Liêm, Hà Nội', 'Male', NULL, 'Active', '2025-04-15 10:15:00', '2025-08-17 10:45:00'),
    ('Đỗ Thị Yến', '0977777888', 'yen.do@gmail.com', '12345678', '1994-11-11',
     '444 Đường Trần Duy Hưng, Quận Cầu Giấy, Hà Nội', 'Female', NULL, 'Active', '2025-04-20 14:20:00', '2025-08-18 16:00:00'),
    ('Ngô Quang Khải', '0988888999', 'khai.ngo@gmail.com', '12345678', '1986-03-30',
     '555 Phố Huế, Quận Hai Bà Trưng, Hà Nội', 'Male', NULL, 'Active', '2025-04-25 12:00:00', '2025-08-20 09:30:00'),
    ('Vũ Thị Hoa', '0999999000', 'hoa.vu@gmail.com', '12345678', '1997-01-05',
     '666 Đường Nguyễn Khánh Toàn, Quận Cầu Giấy, Hà Nội', 'Female', NULL, 'Active', '2025-05-01 15:30:00', '2025-08-21 11:00:00'),
    ('Phan Văn Kiên', '0912121212', 'kien.phan@gmail.com', '12345678', '1992-06-22',
     '777 Đường Bạch Mai, Quận Hai Bà Trưng, Hà Nội', 'Male', NULL, 'Active', '2025-05-05 09:00:00', '2025-08-22 13:00:00'),
    ('Mai Thị Thuỷ', '0923232323', 'thuy.mai@gmail.com', '12345678', '1999-04-14',
     '888 Đường Lê Duẩn, Quận Hoàn Kiếm, Hà Nội', 'Female', NULL, 'Active', '2025-05-10 10:10:00', '2025-08-23 14:15:00');

-- Doctor
INSERT INTO doctors (account_id, specialty, level)
VALUES
    (3, 'Chuyên khoa nội tổng quát', 'Bác sĩ CKI'),
    (5, 'Chuyên khoa tim mạch', 'Thạc sĩ, Bác sĩ'),
    (13, 'Chuyên khoa nhi', 'Bác sĩ CKII'),
    (15, 'Chuyên khoa sản', 'Tiến sĩ, Bác sĩ');

-- Consultant
INSERT INTO consultants (customer_count, note, account_id) VALUES
                                                               (25, 'Chuyên tư vấn liệu trình chăm sóc da cơ bản', 6),
                                                               (40, 'Tư vấn chuyên sâu về điều trị nám và mụn', 9);


-- Receptionists
INSERT INTO receptionists (account_id)
VALUES (4), (12);

-- Department
INSERT INTO departments (department_id, department_name)
VALUES
    (1, 'Xét nghiệm máu'),
    (2, 'Xét nghiệm vi sinh'),
    (3, 'Chẩn đoán hình ảnh');


-- Lab Staff
INSERT INTO lab_staff (account_id, department_id)
VALUES
    (7, 1),
    (16, 2);


-- Cashiers
INSERT INTO cashiers (account_id)
VALUES (8), (18);

-- Patients (các account còn lại)
INSERT INTO patients (passport_number, occupation, account_id) VALUES
                                                                   ('P123456789', 'Sinh viên', 10),
                                                                   ('P987654321', 'Nhân viên văn phòng', 11),
                                                                   ('P456789123', 'Giáo viên', 14),
                                                                   ('P321654987', 'Kinh doanh tự do', 17),
                                                                   ('P741852963', 'Kỹ sư phần mềm', 19),
                                                                   ('P159753486', 'Nội trợ', 20);



INSERT INTO reasons (reason_id, title, content, img) VALUES
                                                 (1, 'Kinh nghiệm lâu năm', 'Đội ngũ bác sĩ với nhiều năm kinh nghiệm.', 'https://i.pinimg.com/736x/07/cd/c1/07cdc13407209b16816d5ed4460f3874.jpg'),
                                                 (2, 'Trang thiết bị hiện đại', 'Ứng dụng công nghệ tiên tiến hàng đầu.', 'https://i.pinimg.com/736x/07/cd/c1/07cdc13407209b16816d5ed4460f3874.jpg'),
                                                 (3, 'Dịch vụ tận tâm', 'Chăm sóc khách hàng chu đáo, chuyên nghiệp.', 'https://i.pinimg.com/736x/07/cd/c1/07cdc13407209b16816d5ed4460f3874.jpg');

INSERT INTO testimonials (content, img, account_id) VALUES
                                                       ('Dịch vụ tuyệt vời, tôi rất hài lòng!', null, 10),
                                                       ('Bác sĩ tận tâm, kết quả ngoài mong đợi.', null, 11),
                                                       ('Không gian sang trọng, cảm giác thoải mái.', null, 14);

INSERT INTO offers (title, `desc`, img) VALUES
                                                       ('Giảm 50% Nâng mũi', 'Tạo dáng mũi chuẩn Hàn Quốc', 'https://i.pinimg.com/736x/0e/cf/07/0ecf07040c7806a0b017331a5d75d661.jpg'),
                                                       ('Ưu đãi Cắt mí', 'Đôi mắt to tròn, tự nhiên', 'https://i.pinimg.com/736x/5f/76/46/5f7646bf405c0f585b60bd97b0080162.jpg'),
                                                       ('Trị nám - trẻ hóa', 'Công nghệ Laser hiện đại', 'https://i.pinimg.com/736x/07/cd/c1/07cdc13407209b16816d5ed4460f3874.jpg'),
                                                       ('Combo dưỡng da', 'Chăm sóc chuyên sâu', 'https://i.pinimg.com/736x/bb/46/3f/bb463f961c29cb926d01b1a4f3a2ca0d.jpg'),
                                                       ('Khuyến mãi triệt lông', 'Hiệu quả lâu dài', 'https://i.pinimg.com/736x/07/cd/c1/07cdc13407209b16816d5ed4460f3874.jpg'),
                                                       ('Giảm béo công nghệ cao', 'An toàn, không xâm lấn', 'https://i.pinimg.com/736x/07/cd/c1/07cdc13407209b16816d5ed4460f3874.jpg');


INSERT INTO appointments (patient_id, time, date, note, status, doctor_id)
VALUES
(1, '09:00', '2025-09-01', 'Khám da liễu tổng quát', 'Pending', 1),

(2, '10:30', '2025-09-02', 'Tư vấn nâng mũi', 'Confirmed', 2),

(3, '14:00', '2025-09-03', 'Điều trị nám', 'Pending', 3),

(4, '15:30', '2025-09-04', 'Tái khám sau điều trị', 'Canceled', 4),

(5, '11:00', '2025-09-05', 'Khám mụn trứng cá', 'Confirmed', 1),

(6, '16:00', '2025-09-06', 'Tư vấn trị sẹo rỗ', 'Pending', 2);
