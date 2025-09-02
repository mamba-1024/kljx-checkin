-- 创建attendance_summary表
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