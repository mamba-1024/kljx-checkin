-- 创建EmployeeDetails表并初始化数据
-- 对应BackendEmployeeController中的/detail/{id}接口

-- 1. 创建employee_details表
CREATE TABLE IF NOT EXISTS `employee_details` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '员工详情ID，主键，自增',
    `employee_id` INT NOT NULL COMMENT '员工ID，关联employee表',
    `id_card` VARCHAR(18) COMMENT '员工身份证号码',
    `bank_card` VARCHAR(20) COMMENT '员工银行卡号码',
    `bank_branch` VARCHAR(255) COMMENT '员工银行卡所属支行信息',
    `bank_reserve_phone` VARCHAR(20) COMMENT '员工银行卡预留手机号码',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` VARCHAR(255) COMMENT '创建人',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `update_by` VARCHAR(255) COMMENT '修改人',
    `name` VARCHAR(255) COMMENT '姓名',
    `id_card_front_url` VARCHAR(500) COMMENT '身份证正面照片URL',
    `id_card_backend_url` VARCHAR(500) COMMENT '身份证背面照片URL',
    INDEX `idx_employee_id` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工详细信息表';

-- 2. 初始化员工详细信息数据（根据employee表中的实际数据）
INSERT INTO `employee_details` (
    `employee_id`, `id_card`, `bank_card`, `bank_branch`, `bank_reserve_phone`, 
    `create_by`, `name`, `id_card_front_url`, `id_card_backend_url`
) VALUES 
-- 对应employee表中的13个员工（根据实际数据）
(1, '330102199001011234', '6222021234567890123', '中国工商银行杭州分行', '13800138001', 
 'system', '张三', 'idcard_front_1.jpg', 'idcard_back_1.jpg'),

(2, '330102199002022345', '6222021234567890124', '中国建设银行杭州分行', '13800138002', 
 'system', '李四', 'idcard_front_2.jpg', 'idcard_back_2.jpg'),

(3, '330102199003033456', '6222021234567890125', '中国农业银行杭州分行', '13800138003', 
 'system', '王五', 'idcard_front_3.jpg', 'idcard_back_3.jpg'),

(4, '330102199004044567', '6222021234567890126', '中国银行杭州分行', '13800138004', 
 'system', '赵六', 'idcard_front_4.jpg', 'idcard_back_4.jpg'),

(5, '330102199005055678', '6222021234567890127', '招商银行杭州分行', '13800138005', 
 'system', '钱七', 'idcard_front_5.jpg', 'idcard_back_5.jpg'),

(6, '330102199006066789', '6222021234567890128', '浦发银行杭州分行', '13800138006', 
 'system', '孙八', 'idcard_front_6.jpg', 'idcard_back_6.jpg'),

(7, '330102199007077890', '6222021234567890129', '中信银行杭州分行', '13800138007', 
 'system', '周九', 'idcard_front_7.jpg', 'idcard_back_7.jpg'),

(8, '330102199008088901', '6222021234567890130', '民生银行杭州分行', '13800138008', 
 'system', '吴十', 'idcard_front_8.jpg', 'idcard_back_8.jpg'),

(9, '330102199009099012', '6222021234567890131', '兴业银行杭州分行', '13800138009', 
 'system', '郑十一', 'idcard_front_9.jpg', 'idcard_back_9.jpg'),

(10, '330102199010100123', '6222021234567890132', '平安银行杭州分行', '13800138010', 
 'system', '王十二', 'idcard_front_10.jpg', 'idcard_back_10.jpg'),

(11, '330102199011111234', '6222021234567890133', '华夏银行杭州分行', '13800138011', 
 'system', '待审核员工1', 'idcard_front_11.jpg', 'idcard_back_11.jpg'),

(12, '330102199012122345', '6222021234567890134', '广发银行杭州分行', '13800138012', 
 'system', '待审核员工2', 'idcard_front_12.jpg', 'idcard_back_12.jpg'),

(13, '330102199013133456', '6222021234567890135', '光大银行杭州分行', '13800138013', 
 'system', '待审核员工3', 'idcard_front_13.jpg', 'idcard_back_13.jpg');

-- 3. 验证数据
-- 查看所有员工详细信息
SELECT * FROM employee_details;

-- 查看员工详细信息与员工基本信息的关联
SELECT 
    e.id,
    e.name AS employee_name,
    e.phone AS employee_phone,
    e.level AS employee_level,
    e.points AS employee_points,
    ed.id_card,
    ed.bank_card,
    ed.bank_branch,
    ed.id_card_front_url,
    ed.id_card_backend_url
FROM employee e
LEFT JOIN employee_details ed ON e.id = ed.employee_id
ORDER BY e.id;

-- 查看员工详细信息数量统计
SELECT 
    COUNT(*) AS total_details_count,
    COUNT(CASE WHEN id_card IS NOT NULL THEN 1 END) AS with_id_card_count,
    COUNT(CASE WHEN bank_card IS NOT NULL THEN 1 END) AS with_bank_card_count
FROM employee_details;

-- 查看银行分布统计
SELECT 
    bank_branch,
    COUNT(*) AS employee_count
FROM employee_details 
WHERE bank_branch IS NOT NULL
GROUP BY bank_branch
ORDER BY employee_count DESC;

-- 查看待审核员工的详细信息
SELECT 
    e.id,
    e.name,
    e.audit_status,
    ed.id_card,
    ed.bank_card,
    ed.bank_branch
FROM employee e
LEFT JOIN employee_details ed ON e.id = ed.employee_id
WHERE e.audit_status = 'WAIT_AUDIT'
ORDER BY e.id; 