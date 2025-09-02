-- 创建所有缺失的表

-- 1. 创建work_shift表
CREATE TABLE IF NOT EXISTS `work_shift` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '班次ID，主键，自增',
  `shift_name` varchar(100) NOT NULL COMMENT '班次名称',
  `start_time` time NOT NULL COMMENT '上班时间',
  `end_time` time NOT NULL COMMENT '下班时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，默认为当前时间',
  `create_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间，默认为当前时间，且每次更新时自动更新',
  `update_by` varchar(100) DEFAULT NULL COMMENT '修改人',
  `exclude_start` time DEFAULT NULL COMMENT '班次排除开始时间',
  `exclude_end` time DEFAULT NULL COMMENT '班次排除结束时间',
  `start_limit` time DEFAULT NULL COMMENT '最早打卡时间',
  `is_summer` tinyint(1) DEFAULT '0' COMMENT '是否为夏令时',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='班次表';

-- 2. 创建attendance_record表
CREATE TABLE IF NOT EXISTS `attendance_record` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '打卡记录ID，主键，自增',
  `employee_id` int NOT NULL COMMENT '员工ID，外键，关联employee表的id字段',
  `punch_time` datetime NOT NULL COMMENT '打卡时间',
  `punch_type` int NOT NULL COMMENT '打卡类型，0表示上班打卡，1表示下班打卡',
  `location` varchar(255) DEFAULT NULL COMMENT '打卡地点',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，默认为当前时间',
  `create_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间，默认为当前时间，且每次更新时自动更新',
  `update_by` varchar(100) DEFAULT NULL COMMENT '修改人',
  `work_shift_id` int DEFAULT NULL COMMENT '班次ID，关联work_shift表的id字段',
  `punch_date` date DEFAULT NULL COMMENT '打卡日期',
  PRIMARY KEY (`id`),
  KEY `idx_employee_id` (`employee_id`),
  KEY `idx_punch_date` (`punch_date`),
  KEY `idx_work_shift_id` (`work_shift_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='打卡记录表';

-- 3. 创建attendance_count表
CREATE TABLE IF NOT EXISTS `attendance_count` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '统计记录ID，主键，自增',
  `employee_id` int NOT NULL COMMENT '员工ID，外键，关联employee表的id字段',
  `attendance_date` date NOT NULL COMMENT '考勤日期',
  `punch_in_times` int DEFAULT '0' COMMENT '上班打卡次数',
  `punch_out_times` int DEFAULT '0' COMMENT '下班打卡次数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，默认为当前时间',
  `create_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间，默认为当前时间，且每次更新时自动更新',
  `update_by` varchar(100) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  KEY `idx_employee_id` (`employee_id`),
  KEY `idx_attendance_date` (`attendance_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='考勤统计表';

-- 4. 创建attendance_summary表
CREATE TABLE IF NOT EXISTS `attendance_summary` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '统计记录ID，主键，自增',
  `employee_id` int NOT NULL COMMENT '员工ID，外键，关联employee表的id字段',
  `attendance_date` date NOT NULL COMMENT '考勤日期',
  `work_shift_id` int DEFAULT NULL COMMENT '班次ID，外键，关联work_shift表的id字段',
  `is_late` tinyint(1) DEFAULT '0' COMMENT '是否迟到，0表示否，1表示是',
  `is_early_leave` tinyint(1) DEFAULT '0' COMMENT '是否早退，0表示否，1表示是',
  `is_absent` tinyint(1) DEFAULT '0' COMMENT '是否缺勤，0表示否，1表示是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，默认为当前时间',
  `create_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间，默认为当前时间，且每次更新时自动更新',
  `update_by` varchar(100) DEFAULT NULL COMMENT '修改人',
  `work_hours` decimal(10,2) DEFAULT '0.00' COMMENT '工作时长（小时）',
  PRIMARY KEY (`id`),
  KEY `idx_employee_id` (`employee_id`),
  KEY `idx_attendance_date` (`attendance_date`),
  KEY `idx_work_shift_id` (`work_shift_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='考勤汇总表';

-- 插入work_shift初始数据
-- 注意：根据JudgeWorkShiftUtil.isCommonShift()方法，ID为1和3的班次是普通班次，其他是加班班次
INSERT INTO `work_shift` (`id`, `shift_name`, `start_time`, `end_time`, `create_by`, `start_limit`, `exclude_start`, `exclude_end`, `is_summer`) VALUES
-- 普通班次（冬令时）- 7:00-17:00，6:00开始可打卡，11:30-12:00午餐时间
(1, '白班', '07:00:00', '17:00:00', 'system', '06:00:00', '11:30:00', '12:00:00', 0),
(2, '加班', '17:30:00', '20:00:00', 'system', '17:00:00', NULL, NULL, 0),
(3, '白班', '07:00:00', '17:00:00', 'system', '06:00:00', '11:30:00', '12:00:00', 0),
-- 普通班次（夏令时）- 7:00-17:30，6:00开始可打卡，11:30-12:30午餐时间
(4, '夏令白班', '07:00:00', '17:30:00', 'system', '06:00:00', '11:30:00', '12:30:00', 1),
(5, '夏令加班', '18:00:00', '20:30:00', 'system', '17:30:00', NULL, NULL, 1),
(6, '夏令白班', '07:00:00', '17:30:00', 'system', '06:00:00', '11:30:00', '12:30:00', 1); 