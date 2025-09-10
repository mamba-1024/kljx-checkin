-- 创建work_shift表
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='班次表（四次打卡：7:30/11:30/13:00/17:00）';

-- 插入初始数据
-- 注意：根据JudgeWorkShiftUtil.isCommonShift()方法，ID为1和3的班次是普通班次，其他是加班班次
INSERT INTO `work_shift` (`id`, `shift_name`, `start_time`, `end_time`, `create_by`, `exclude_start`, `exclude_end`, `start_limit`, `is_summer`) VALUES
-- 普通班次（冬令时）- 上午7:30-11:30，下午13:00-17:00，6:30开始可打卡
("1","上午","07:30:00","11:30:00","system","11:30:00","13:00:00","06:30:00","0"),
("2","下午","07:30:00","11:30:00","system","11:30:00","13:00:00","12:30:00","0"),
("3","加班-晚间","17:30:00","20:30:00","system","17:00:00","17:30:00","17:10:00","0"),
("4","上午","07:30:00","11:30:00","system","11:30:00","13:00:00","06:30:00","1"),
("5","下午","07:30:00","11:30:00","system","11:30:00","13:00:00","12:30:00","1"),
("6","加班-晚间","17:30:00","20:30:00","system","17:00:00","17:30:00","17:10:00","1")

