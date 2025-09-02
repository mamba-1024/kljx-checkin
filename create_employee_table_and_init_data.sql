-- 初始化/static接口涉及的数据表和数据
-- 涉及的表：employee, employee_attendance, hzjy_config

-- 1. 创建employee表
CREATE TABLE IF NOT EXISTS `employee` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '员工ID，主键，自增',
    `name` VARCHAR(255) NOT NULL COMMENT '员工姓名',
    `avatar` VARCHAR(500) COMMENT '员工头像',
    `status` BOOLEAN DEFAULT TRUE COMMENT '员工启停用状态',
    `on_board` BOOLEAN DEFAULT TRUE COMMENT '员工在职状态',
    `is_authenticated` BOOLEAN DEFAULT TRUE COMMENT '员工是否实名认证',
    `level` INT DEFAULT 0 COMMENT '员工当前等级',
    `phone` VARCHAR(20) COMMENT '员工手机号码',
    `password` VARCHAR(255) COMMENT '员工密码',
    `salt` VARCHAR(255) COMMENT '密码加密盐',
    `nickname` VARCHAR(255) COMMENT '员工昵称',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` VARCHAR(255) COMMENT '创建人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `update_by` VARCHAR(255) COMMENT '修改人',
    `points` INT DEFAULT 0 COMMENT '积分',
    `total_work_time` DECIMAL(10,2) DEFAULT 0.00 COMMENT '当前安全上工总时长',
    `current_month_time` DECIMAL(10,2) DEFAULT 0.00 COMMENT '当月累计工时',
    `left_work_time` DECIMAL(10,2) DEFAULT 0.00 COMMENT '剩余工时',
    `open_id` VARCHAR(255) COMMENT '微信openId',
    `onboarding_date` DATETIME COMMENT '入职时间',
    `enable_date` DATETIME COMMENT '启用时间',
    `disable_date` DATETIME COMMENT '停用时间',
    `resign_date` DATETIME COMMENT '离职时间',
    `audit_status` VARCHAR(20) DEFAULT 'WAIT_AUDIT' COMMENT '审核状态',
    `reject_reason` TEXT COMMENT '拒绝原因'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- 2. 创建employee_attendance表
CREATE TABLE IF NOT EXISTS `employee_attendance` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `employee_id` INT NOT NULL COMMENT '员工ID',
    `punch_date` DATE NOT NULL COMMENT '打卡日期',
    `common_punch_start` DATETIME COMMENT '普通打卡开始时间',
    `common_punch_end` DATETIME COMMENT '普通打卡结束时间',
    `common_work_shift_id` INT COMMENT '普通打卡班次ID',
    `common_punch_duration` DECIMAL(10,2) DEFAULT 0.00 COMMENT '普通打卡时长',
    `over_punch_start` DATETIME COMMENT '加班打卡开始时间',
    `over_punch_end` DATETIME COMMENT '加班打卡结束时间',
    `over_punch_duration` DECIMAL(10,2) DEFAULT 0.00 COMMENT '加班打卡总时长',
    `over_work_shift_id` INT COMMENT '加班打卡班次ID',
    `create_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_employee_id` (`employee_id`),
    INDEX `idx_punch_date` (`punch_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工考勤表';

-- 3. 创建hzjy_config表
CREATE TABLE IF NOT EXISTS `hzjy_config` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '配置ID',
    `main_url` TEXT COMMENT '主图URL配置'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 4. 初始化员工数据（用于测试/static接口）
INSERT INTO `employee` (
    `name`, `avatar`, `status`, `on_board`, `is_authenticated`, `level`, 
    `phone`, `password`, `nickname`, `points`, `total_work_time`, 
    `current_month_time`, `audit_status`, `enable_date`, `onboarding_date`
) VALUES 
-- 已启用员工（昨日启用）
('张三', 'https://example.com/avatar1.jpg', TRUE, TRUE, TRUE, 2, 
 '13800138001', 'password123', '小张', 150, 120.50, 45.20, 'PASS', 
 DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY)),

-- 已停用员工（昨日停用）
('李四', 'https://example.com/avatar2.jpg', FALSE, FALSE, TRUE, 1, 
 '13800138002', 'password123', '小李', 80, 95.30, 0.00, 'PASS', 
 DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 40 DAY)),

-- 新员工（昨日入职）
('王五', 'https://example.com/avatar3.jpg', TRUE, TRUE, TRUE, 0, 
 '13800138003', 'password123', '小王', 0, 0.00, 0.00, 'PASS', 
 DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 1 DAY)),

-- 积分排名前十的员工
('赵六', 'https://example.com/avatar4.jpg', TRUE, TRUE, TRUE, 3, 
 '13800138004', 'password123', '小赵', 300, 200.00, 60.00, 'PASS', 
 DATE_SUB(NOW(), INTERVAL 15 DAY), DATE_SUB(NOW(), INTERVAL 45 DAY)),

('钱七', 'https://example.com/avatar5.jpg', TRUE, TRUE, TRUE, 3, 
 '13800138005', 'password123', '小钱', 280, 180.50, 55.30, 'PASS', 
 DATE_SUB(NOW(), INTERVAL 20 DAY), DATE_SUB(NOW(), INTERVAL 50 DAY)),

('孙八', 'https://example.com/avatar6.jpg', TRUE, TRUE, TRUE, 2, 
 '13800138006', 'password123', '小孙', 250, 160.20, 50.10, 'PASS', 
 DATE_SUB(NOW(), INTERVAL 25 DAY), DATE_SUB(NOW(), INTERVAL 55 DAY)),

('周九', 'https://example.com/avatar7.jpg', TRUE, TRUE, TRUE, 2, 
 '13800138007', 'password123', '小周', 220, 140.80, 45.60, 'PASS', 
 DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 60 DAY)),

('吴十', 'https://example.com/avatar8.jpg', TRUE, TRUE, TRUE, 1, 
 '13800138008', 'password123', '小吴', 200, 120.40, 40.20, 'PASS', 
 DATE_SUB(NOW(), INTERVAL 35 DAY), DATE_SUB(NOW(), INTERVAL 65 DAY)),

('郑十一', 'https://example.com/avatar9.jpg', TRUE, TRUE, TRUE, 1, 
 '13800138009', 'password123', '小郑', 180, 100.60, 35.80, 'PASS', 
 DATE_SUB(NOW(), INTERVAL 40 DAY), DATE_SUB(NOW(), INTERVAL 70 DAY)),

('王十二', 'https://example.com/avatar10.jpg', TRUE, TRUE, TRUE, 0, 
 '13800138010', 'password123', '小王', 160, 80.90, 30.50, 'PASS', 
 DATE_SUB(NOW(), INTERVAL 45 DAY), DATE_SUB(NOW(), INTERVAL 75 DAY)),

-- 待审核员工
('待审核员工1', 'https://example.com/avatar11.jpg', FALSE, FALSE, TRUE, 0, 
 '13800138011', 'password123', '待审核1', 0, 0.00, 0.00, 'WAIT_AUDIT', 
 NULL, NULL),

('待审核员工2', 'https://example.com/avatar12.jpg', FALSE, FALSE, TRUE, 0, 
 '13800138012', 'password123', '待审核2', 0, 0.00, 0.00, 'WAIT_AUDIT', 
 NULL, NULL),

('待审核员工3', 'https://example.com/avatar13.jpg', FALSE, FALSE, TRUE, 0, 
 '13800138013', 'password123', '待审核3', 0, 0.00, 0.00, 'WAIT_AUDIT', 
 NULL, NULL);

-- 5. 初始化员工考勤数据（昨日数据）
INSERT INTO `employee_attendance` (
    `employee_id`, `punch_date`, `common_punch_start`, `common_punch_end`, 
    `common_punch_duration`, `over_punch_start`, `over_punch_end`, `over_punch_duration`
) VALUES 
-- 昨日正常上班的员工
(1, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 
 DATE_SUB(CURDATE(), INTERVAL 1 DAY) + INTERVAL 9 HOUR, 
 DATE_SUB(CURDATE(), INTERVAL 1 DAY) + INTERVAL 18 HOUR, 
 8.00, NULL, NULL, 0.00),

(4, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 
 DATE_SUB(CURDATE(), INTERVAL 1 DAY) + INTERVAL 8 HOUR, 
 DATE_SUB(CURDATE(), INTERVAL 1 DAY) + INTERVAL 17 HOUR, 
 8.00, NULL, NULL, 0.00),

(5, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 
 DATE_SUB(CURDATE(), INTERVAL 1 DAY) + INTERVAL 8 HOUR + INTERVAL 30 MINUTE, 
 DATE_SUB(CURDATE(), INTERVAL 1 DAY) + INTERVAL 17 HOUR + INTERVAL 30 MINUTE, 
 8.00, NULL, NULL, 0.00),

-- 昨日加班的员工
(6, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 
 DATE_SUB(CURDATE(), INTERVAL 1 DAY) + INTERVAL 9 HOUR, 
 DATE_SUB(CURDATE(), INTERVAL 1 DAY) + INTERVAL 18 HOUR, 
 8.00, 
 DATE_SUB(CURDATE(), INTERVAL 1 DAY) + INTERVAL 18 HOUR, 
 DATE_SUB(CURDATE(), INTERVAL 1 DAY) + INTERVAL 21 HOUR, 
 2.00),

(7, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 
 DATE_SUB(CURDATE(), INTERVAL 1 DAY) + INTERVAL 8 HOUR, 
 DATE_SUB(CURDATE(), INTERVAL 1 DAY) + INTERVAL 17 HOUR, 
 8.00, 
 DATE_SUB(CURDATE(), INTERVAL 1 DAY) + INTERVAL 17 HOUR, 
 DATE_SUB(CURDATE(), INTERVAL 1 DAY) + INTERVAL 20 HOUR, 
 2.50),

(8, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 
 DATE_SUB(CURDATE(), INTERVAL 1 DAY) + INTERVAL 8 HOUR + INTERVAL 30 MINUTE, 
 DATE_SUB(CURDATE(), INTERVAL 1 DAY) + INTERVAL 17 HOUR + INTERVAL 30 MINUTE, 
 8.00, 
 DATE_SUB(CURDATE(), INTERVAL 1 DAY) + INTERVAL 17 HOUR + INTERVAL 30 MINUTE, 
 DATE_SUB(CURDATE(), INTERVAL 1 DAY) + INTERVAL 22 HOUR, 
 3.00);

-- 6. 初始化系统配置
INSERT INTO `hzjy_config` (`id`, `main_url`) VALUES 
(1, '["banner1.jpg", "banner2.jpg", "banner3.jpg"]');

-- 7. 验证数据
-- 查看员工统计
SELECT 
    COUNT(CASE WHEN enable_date = DATE_SUB(CURDATE(), INTERVAL 1 DAY) THEN 1 END) AS yesterday_enabled_count,
    COUNT(CASE WHEN disable_date = DATE_SUB(CURDATE(), INTERVAL 1 DAY) THEN 1 END) AS yesterday_disabled_count,
    COUNT(CASE WHEN DATE(enable_date) = DATE_SUB(CURDATE(), INTERVAL 1 DAY) THEN 1 END) AS yesterday_new_count
FROM employee;

-- 查看等级分布
SELECT 
    CONCAT('LV', level) AS level_display,
    COUNT(*) AS count
FROM employee
GROUP BY level_display;

-- 查看积分排名前十
SELECT name, points, level FROM employee ORDER BY points DESC LIMIT 10;

-- 查看待审核员工数量
SELECT COUNT(*) AS wait_audit_count FROM employee WHERE audit_status = 'WAIT_AUDIT';

-- 查看昨日上班情况
SELECT 
    SUM(common_punch_duration) AS totalCommon, 
    SUM(over_punch_duration) AS totalOver
FROM employee_attendance 
WHERE punch_date = DATE_SUB(CURDATE(), INTERVAL 1 DAY); 