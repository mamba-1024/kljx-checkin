-- 创建attendance_record表
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