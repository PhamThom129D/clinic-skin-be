-- Thêm phòng ban
INSERT IGNORE INTO department ( name) VALUES ( 'IT');
INSERT IGNORE INTO department ( name) VALUES ( 'HR');
INSERT IGNORE INTO department (name) VALUES ( 'Finance');

-- Thêm nhân viên
INSERT IGNORE INTO employee ( name, age , code, salary, department_id) VALUES ( 'Nguyen Van A', 25, "CG1", 1000, 1);
INSERT IGNORE INTO employee ( name, age , code, salary, department_id) VALUES ( 'Tran Thi B', 30,"CG2", 1200, 2);
INSERT IGNORE INTO employee (name, age , code, salary, department_id) VALUES ( 'Le Van C', 28,"CG3", 1500, 3);
INSERT IGNORE INTO employee ( name, age , code, salary, department_id) VALUES ( 'Pham Thi D', 26,"CG4", 1100, 1);
INSERT IGNORE INTO employee (name, age , code, salary, department_id) VALUES ('Hoang Van E', 35,"CG5", 2000, 3);
