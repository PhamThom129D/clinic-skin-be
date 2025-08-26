# -- -------------------------------
# -- 1. Roles
# -- -------------------------------
# INSERT INTO roles (role_id, role_name, description) VALUES
#                                                         (1, 'ROLE_ADMIN', 'Quản trị hệ thống'),
#                                                         (2, 'ROLE_PATIENT', 'Bệnh nhân'),
#                                                         (3, 'ROLE_DOCTOR', 'Bác sĩ'),
#                                                         (4, 'ROLE_RECEPTIONIST', 'Nhân viên tiếp đón'),
#                                                         (5, 'ROLE_LAB_STAFF', 'Nhân viên xét nghiệm'),
#                                                         (6, 'ROLE_CONSULTANT', 'Nhân viên tư vấn'),
#                                                         (7, 'ROLE_CASHIER', 'Nhân viên thu ngân')
#     ON DUPLICATE KEY UPDATE role_name = role_name;
# INSERT IGNORE INTO permissions (name, description) VALUES
# ('MANAGE_USERS', 'Quản lý tài khoản người dùng'),
# ('MANAGE_ROLES', 'Quản lý vai trò'),
# ('VIEW_REPORTS', 'Xem báo cáo'),
# ('CREATE_APPOINTMENT', 'Tạo lịch hẹn'),
# ('UPDATE_APPOINTMENT', 'Cập nhật lịch hẹn'),
# ('DELETE_APPOINTMENT', 'Xóa lịch hẹn'),
# ('VIEW_LAB_RESULTS', 'Xem kết quả xét nghiệm');
#
#
# -- ROLE_ADMIN: tất cả quyền
# INSERT IGNORE INTO role_permissions (role_id, permission_id) VALUES
#                                                           (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7);
#
# -- ROLE_PATIENT: chỉ xem báo cáo
# INSERT IGNORE INTO role_permissions (role_id, permission_id) VALUES
#     (2, 3);
#
# -- ROLE_DOCTOR: xem báo cáo, xem kết quả xét nghiệm
# INSERT IGNORE INTO role_permissions (role_id, permission_id) VALUES
#                                                           (3, 3), (3, 7);
#
# -- ROLE_RECEPTIONIST: tạo/cập nhật/xóa lịch hẹn
# INSERT IGNORE INTO role_permissions (role_id, permission_id) VALUES
#                                                           (4, 4), (4, 5), (4, 6);
#
# -- ROLE_LAB_STAFF: xem kết quả xét nghiệm
# INSERT IGNORE INTO role_permissions (role_id, permission_id) VALUES
#     (5, 7);
#
# -- ROLE_CONSULTANT: tạo/cập nhật lịch hẹn, xem báo cáo
# INSERT IGNORE INTO role_permissions (role_id, permission_id) VALUES
#                                                           (6, 3), (6, 4), (6, 5);
#
# -- ROLE_CASHIER: xem báo cáo
# INSERT IGNORE INTO role_permissions (role_id, permission_id) VALUES
#     (7, 3);
