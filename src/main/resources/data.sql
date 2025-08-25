INSERT INTO roles (role_id, role_name, description) VALUES
                                                        (1, 'ROLE_ADMIN', 'Quản trị hệ thống'),
                                                        (2, 'ROLE_PATIENT', 'Bệnh nhân'),
                                                        (3, 'ROLE_DOCTOR', 'Bác sĩ'),
                                                        (4, 'ROLE_RECEPTIONIST', 'Nhân viên tiếp đón'),
                                                        (5, 'ROLE_LAB_STAFF', 'Nhân viên xét nghiệm'),
                                                        (6, 'ROLE_CONSULTANT', 'Nhân viên tư vấn'),
                                                        (7, 'ROLE_CASHIER', 'Nhân viên thu ngân')
    ON DUPLICATE KEY UPDATE role_name = role_name;
