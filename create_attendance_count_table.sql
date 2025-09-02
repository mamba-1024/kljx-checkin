-- 创建attendance_count表
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