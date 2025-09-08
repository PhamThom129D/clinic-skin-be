-- =====================================================
-- 1. Roles
-- =====================================================
INSERT IGNORE INTO roles (role_id, role_name, description)
VALUES
    (1, 'ROLE_ADMIN', 'Quản trị hệ thống'),
    (2, 'ROLE_PATIENT', 'Bệnh nhân'),
    (3, 'ROLE_DOCTOR', 'Bác sĩ'),
    (4, 'ROLE_RECEPTIONIST', 'Nhân viên tiếp đón'),
    (5, 'ROLE_LAB_STAFF', 'Nhân viên xét nghiệm'),
    (6, 'ROLE_CONSULTANT', 'Nhân viên tư vấn'),
    (7, 'ROLE_CASHIER', 'Nhân viên thu ngân')
ON DUPLICATE KEY UPDATE role_name = role_name;

-- =====================================================
-- 2. Accounts
-- =====================================================
INSERT IGNORE INTO accounts
(full_name, phone_number, email, password, date_of_birth, address, gender, avt_path, status, created_at, updated_at)
VALUES
-- Admin / Doctors / Staff / Patients
('Nguyễn Văn Hậu', '0911111222', 'hau.nguyen@gmail.com', '$2a$12$DQNFuLRhy89KzmM8NYzVIuyIT6Jw3xFzfIlyDKE9BF5Y.QbhdhtF.', '1993-12-15', '808 Đường Nguyễn Chí Thanh, Quận Đống Đa, Hà Nội', 'MALE', 'https://res.cloudinary.com/dk6vu2mlh/image/upload/v1757312220/doctor_ldwduy.png', 'Active', '2025-03-20 10:00:00', '2025-08-10 12:00:00'),
('Trần Thị Hồng', '0933333444', 'hong.tran@gmail.com', '$2a$12$DQNFuLRhy89KzmM8NYzVIuyIT6Jw3xFzfIlyDKE9BF5Y.QbhdhtF.', '1996-09-09', '909 Đường Cầu Giấy, Quận Cầu Giấy, Hà Nội', 'FEMALE', 'https://res.cloudinary.com/dk6vu2mlh/image/upload/v1757312220/doctor_ldwduy.png', 'Active', '2025-03-25 11:00:00', '2025-08-12 13:30:00'),
('Phạm Văn Dũng', '0944444555', 'dung.pham@gmail.com', '$2a$12$DQNFuLRhy89KzmM8NYzVIuyIT6Jw3xFzfIlyDKE9BF5Y.QbhdhtF.', '1982-02-28', '111 Phố Tây Sơn, Quận Đống Đa, Hà Nội', 'MALE', 'https://res.cloudinary.com/dk6vu2mlh/image/upload/v1757312220/doctor_ldwduy.png', 'Active', '2025-04-01 09:45:00', '2025-08-14 14:20:00'),
('Lê Thị Ngọc', '0955555666', 'ngoc.le@gmail.com', '$2a$12$DQNFuLRhy89KzmM8NYzVIuyIT6Jw3xFzfIlyDKE9BF5Y.QbhdhtF.', '1990-05-18', '222 Đường Nguyễn Văn Cừ, Quận Long Biên, Hà Nội', 'FEMALE', 'https://res.cloudinary.com/dk6vu2mlh/image/upload/v1757312220/doctor_ldwduy.png', 'Active', '2025-04-10 08:30:00', '2025-08-16 15:10:00'),
('Hoàng Văn Hải', '0966666777', 'hai.hoang@gmail.com', '$2a$12$DQNFuLRhy89KzmM8NYzVIuyIT6Jw3xFzfIlyDKE9BF5Y.QbhdhtF.', '1987-07-25', '333 Đường Phạm Hùng, Quận Nam Từ Liêm, Hà Nội', 'MALE', 'https://i.imgur.com/staff1.png', 'Active', '2025-04-15 10:15:00', '2025-08-17 10:45:00'),
('Đỗ Thị Yến', '0977777888', 'yen.do@gmail.com', '$2a$12$DQNFuLRhy89KzmM8NYzVIuyIT6Jw3xFzfIlyDKE9BF5Y.QbhdhtF.', '1994-11-11', '444 Đường Trần Duy Hưng, Quận Cầu Giấy, Hà Nội', 'FEMALE', 'https://i.imgur.com/staff2.png', 'Active', '2025-04-20 14:20:00', '2025-08-18 16:00:00'),
('Ngô Quang Khải', '0988888999', 'khai.ngo@gmail.com', '$2a$12$DQNFuLRhy89KzmM8NYzVIuyIT6Jw3xFzfIlyDKE9BF5Y.QbhdhtF.', '1986-03-30', '555 Phố Huế, Quận Hai Bà Trưng, Hà Nội', 'MALE', 'https://i.imgur.com/staff3.png', 'Active', '2025-04-25 12:00:00', '2025-08-20 09:30:00'),
('Vũ Thị Hoa', '0999999000', 'hoa.vu@gmail.com', '$2a$12$DQNFuLRhy89KzmM8NYzVIuyIT6Jw3xFzfIlyDKE9BF5Y.QbhdhtF.', '1997-01-05', '666 Đường Nguyễn Khánh Toàn, Quận Cầu Giấy, Hà Nội', 'FEMALE', 'https://i.imgur.com/staff4.png', 'Active', '2025-05-01 15:30:00', '2025-08-21 11:00:00'),
('Phan Văn Kiên', '0912121212', 'kien.phan@gmail.com', '$2a$12$DQNFuLRhy89KzmM8NYzVIuyIT6Jw3xFzfIlyDKE9BF5Y.QbhdhtF.', '1992-06-22', '777 Đường Bạch Mai, Quận Hai Bà Trưng, Hà Nội', 'MALE', NULL, 'Active', '2025-05-05 09:00:00', '2025-08-22 13:00:00'),
('Mai Thị Thuỷ', '0923232323', 'thuy.mai@gmail.com', '$2a$12$DQNFuLRhy89KzmM8NYzVIuyIT6Jw3xFzfIlyDKE9BF5Y.QbhdhtF.', '1999-04-14', '888 Đường Lê Duẩn, Quận Hoàn Kiếm, Hà Nội', 'FEMALE', NULL, 'Active', '2025-05-10 10:10:00', '2025-08-23 14:15:00'),
('Nguyễn Văn Nam', '0912345678', 'nam.nguyen@gmail.com', '$2a$12$DQNFuLRhy89KzmM8NYzVIuyIT6Jw3xFzfIlyDKE9BF5Y.QbhdhtF.', '1988-02-25', '123 Phố Tràng Tiền, Quận Hoàn Kiếm, Hà Nội', 'MALE', NULL, 'Active', '2024-05-10 10:00:00', '2025-07-20 15:30:00'),
('Phạm Thị Thu', '0987654321', 'thu.pham@gmail.com', '$2a$12$DQNFuLRhy89KzmM8NYzVIuyIT6Jw3xFzfIlyDKE9BF5Y.QbhdhtF.', '1995-07-10', '456 Phố Bà Triệu, Quận Hai Bà Trưng, Hà Nội', 'FEMALE', NULL, 'Active', '2024-06-15 11:30:00', '2025-08-01 09:45:00'),
('Lê Hoàng Long', '0901122334', 'long.le@gmail.com', '$2a$12$DQNFuLRhy89KzmM8NYzVIuyIT6Jw3xFzfIlyDKE9BF5Y.QbhdhtF.', '2000-11-20', '789 Đường Láng, Quận Đống Đa, Hà Nội', 'MALE', NULL, 'Active', '2024-07-20 14:00:00', '2025-08-25 10:10:00'),
('Trần Thanh Mai', '0934567890', 'maitran@gmail.com', '$2a$12$DQNFuLRhy89KzmM8NYzVIuyIT6Jw3xFzfIlyDKE9BF5Y.QbhdhtF.', '1992-04-12', '101 Phố Hàng Bông, Quận Hoàn Kiếm, Hà Nội', 'FEMALE', NULL, 'Active', '2024-08-01 09:20:00', '2025-06-12 17:05:00'),
('Võ Văn Tuấn', '0967890123', 'tuanvo@gmail.com', '$2a$12$DQNFuLRhy89KzmM8NYzVIuyIT6Jw3xFzfIlyDKE9BF5Y.QbhdhtF.', '1985-09-30', '202 Đường Xuân Thủy, Quận Cầu Giấy, Hà Nội', 'MALE', NULL, 'Active', '2024-09-05 16:45:00', '2025-05-30 11:20:00'),
('Hoàng Thị Lan', '0978901234', 'lan.hoang@gmail.com', '$2a$12$DQNFuLRhy89KzmM8NYzVIuyIT6Jw3xFzfIlyDKE9BF5Y.QbhdhtF.', '1975-06-05', '303 Đường Kim Mã, Quận Ba Đình, Hà Nội', 'FEMALE', NULL, 'Active', '2024-10-20 08:30:00', '2025-04-15 14:00:00'),
('Đinh Quang Minh', '0908765432', 'minhdinh@gmail.com', '$2a$12$DQNFuLRhy89KzmM8NYzVIuyIT6Jw3xFzfIlyDKE9BF5Y.QbhdhtF.', '1998-01-18', '404 Phố Khâm Thiên, Quận Đống Đa, Hà Nội', 'MALE', NULL, 'Active', '2024-11-25 12:15:00', '2025-03-22 09:50:00'),
('Nguyễn Thị Trâm', '0919283746', 'tramnguyen@gmail.com', '$2a$12$DQNFuLRhy89KzmM8NYzVIuyIT6Jw3xFzfIlyDKE9BF5Y.QbhdhtF.', '2001-03-03', '505 Đường Trần Phú, Quận Hà Đông, Hà Nội', 'FEMALE', NULL, 'Active', '2025-01-10 10:30:00', '2025-02-28 15:10:00'),
('Phan Văn Hùng', '0928374650', 'hungphan@gmail.com', '$2a$12$DQNFuLRhy89KzmM8NYzVIuyIT6Jw3xFzfIlyDKE9BF5Y.QbhdhtF.', '1980-12-12', '606 Phố Nguyễn Lương Bằng, Quận Đống Đa, Hà Nội', 'MALE', NULL, 'Active', '2025-02-15 09:45:00', '2025-03-10 14:30:00'),
('Lý Thị Bích', '0938475612', 'bichly@gmail.com', '$2a$12$DQNFuLRhy89KzmM8NYzVIuyIT6Jw3xFzfIlyDKE9BF5Y.QbhdhtF.', '1996-08-21', '707 Đường Nguyễn Trãi, Quận Thanh Xuân, Hà Nội', 'FEMALE', NULL, 'Active', '2025-03-01 11:00:00', '2025-03-20 13:45:00');

-- =====================================================
-- 3. Account Roles
-- =====================================================
INSERT IGNORE INTO account_roles (account_id, role_id)
VALUES
-- Admin
(1, 1),

-- Doctors
(1, 3), (2, 3), (3, 3), (4, 3),

-- Consultants
(6, 6), (9, 6),

-- Receptionists
(5, 4), (8, 4),

-- Lab Staff
(7, 5), (14, 5),

-- Cashiers
(10, 7), (17, 7),

-- Patients (các account còn lại)
(11, 2), (12, 2), (13, 2), (15, 2), (16, 2), (18, 2), (19, 2), (20, 2);

-- =====================================================
-- 4. Doctors
-- =====================================================
INSERT IGNORE INTO doctors (account_id, specialty, level)
VALUES
(1, 'Chuyên khoa nội tổng quát', 'Bác sĩ CKI'),
(2, 'Chuyên khoa tim mạch', 'Thạc sĩ, Bác sĩ'),
(3, 'Chuyên khoa nhi', 'Bác sĩ CKII'),
(4, 'Chuyên khoa sản', 'Tiến sĩ, Bác sĩ');

-- =====================================================
-- 5. Consultants
-- =====================================================
INSERT IGNORE INTO consultants (account_id)
VALUES (6), (9);

-- =====================================================
-- 6. Receptionists
-- =====================================================
INSERT IGNORE INTO receptionists (account_id)
VALUES (5), (8);

-- =====================================================
-- 7. Departments
-- =====================================================
INSERT IGNORE INTO departments (department_id, department_name)
VALUES
(1, 'Xét nghiệm máu'),
(2, 'Xét nghiệm vi sinh'),
(3, 'Chẩn đoán hình ảnh');

-- =====================================================
-- 8. Lab Staff
-- =====================================================
INSERT IGNORE INTO lab_staff (account_id, department_id)
VALUES
(7, 1),
(14, 2);

-- =====================================================
-- 9. Cashiers
-- =====================================================
INSERT IGNORE INTO cashiers (account_id)
VALUES (10), (17);

-- =====================================================
-- 10. Patients
-- =====================================================
INSERT IGNORE INTO patients (passport_number, occupation, account_id)
VALUES
('P123456789', 'Sinh viên', 11),
('P987654321', 'Nhân viên văn phòng', 12),
('P456789123', 'Giáo viên', 13),
('P321654987', 'Kinh doanh tự do', 15),
('P741852963', 'Kỹ sư phần mềm', 16),
('P159753486', 'Nội trợ', 18),
('P258369147', 'Sinh viên', 19),
('P369258147', 'Nhân viên văn phòng', 20);

-- =====================================================
-- 11. Reasons
-- =====================================================
INSERT IGNORE INTO reasons (reason_id, title, content, img)
VALUES
(1, 'Kinh nghiệm lâu năm', 'Đội ngũ bác sĩ với nhiều năm kinh nghiệm.', 'https://i.pinimg.com/736x/07/cd/c1/07cdc13407209b16816d5ed4460f3874.jpg'),
(2, 'Trang thiết bị hiện đại', 'Ứng dụng công nghệ tiên tiến hàng đầu.', 'https://i.pinimg.com/736x/07/cd/c1/07cdc13407209b16816d5ed4460f3874.jpg'),
(3, 'Dịch vụ tận tâm', 'Chăm sóc khách hàng chu đáo, chuyên nghiệp.', 'https://i.pinimg.com/736x/07/cd/c1/07cdc13407209b16816d5ed4460f3874.jpg');

-- =====================================================
-- 12. Testimonials
-- =====================================================
INSERT IGNORE INTO testimonials (content, img, account_id)
VALUES
('Dịch vụ tuyệt vời, tôi rất hài lòng!', NULL, 11),
('Bác sĩ tận tâm, kết quả ngoài mong đợi.', NULL, 12),
('Không gian sang trọng, cảm giác thoải mái.', NULL, 13);

-- =====================================================
-- 13. Offers
-- =====================================================
INSERT IGNORE INTO offers (title, description, img)
VALUES
('Giảm 40% Trị Mụn Chuyên Sâu', 'Liệu trình chuẩn y khoa, giảm viêm mụn, ngăn ngừa tái phát', 'https://i.pinimg.com/736x/cb/1d/af/cb1dafde466c5f541eafdca7d66a4fd8.jpg'),
('Ưu đãi Trị Nám - Tàn Nhang', 'Ứng dụng Laser hiện đại, hiệu quả an toàn', 'https://i.pinimg.com/1200x/b2/9a/9b/b29a9bae161bfe5c14276a21dbb59dac.jpg'),
('Chăm Sóc Da Sau Mụn', 'Phục hồi, giảm thâm sẹo, dưỡng sáng da', 'https://i.pinimg.com/736x/af/fc/f9/affcf9e29970feef712c33a8f3ecf73d.jpg');

-- =====================================================
-- 14. Appointments
-- =====================================================
INSERT IGNORE INTO appointments (patient_id, time, date, note, status, doctor_id)
VALUES
(1, '09:00', '2025-09-01', 'Khám da liễu tổng quát', 'Pending', 1),
(2, '10:30', '2025-09-02', 'Tư vấn nâng mũi', 'Confirmed', 2),
(3, '14:00', '2025-09-03', 'Điều trị nám', 'Pending', 3),
(4, '11:00', '2025-09-04', 'Khám tổng quát', 'Pending', 4),
(5, '15:30', '2025-09-05', 'Tư vấn da mặt', 'Confirmed', 1);

-- =====================================================
-- 15. Contacts
-- =====================================================
INSERT IGNORE INTO contacts (id, fullname, phone, reason)
VALUES
(1, 'Phạm Văn C', '0988123456', 'Muốn tư vấn về trị mụn'),
(2, 'Lê Thị D', '0977123123', 'Quan tâm dịch vụ chăm sóc da');

-- =====================================================
-- 16. Medical Records
-- =====================================================
INSERT IGNORE INTO medical_records
(record_id, patient_id, visit_date, created_at, updated_at)
VALUES
(1, 1, '2025-07-10', '2025-07-10 09:00:00', '2025-07-10 09:00:00'),
(2, 2, '2025-07-15', '2025-07-15 10:30:00', '2025-07-15 10:30:00'),
(3, 3, '2025-07-20', '2025-07-20 11:00:00', '2025-07-20 11:00:00'),
(4, 4, '2025-07-22', '2025-07-22 14:00:00', '2025-07-22 14:00:00'),
(5, 5, '2025-07-25', '2025-07-25 15:00:00', '2025-07-25 15:00:00');
