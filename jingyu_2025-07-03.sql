# ************************************************************
# Sequel Ace SQL dump
# 版本号： 20035
#
# https://sequel-ace.com/
# https://github.com/Sequel-Ace/Sequel-Ace
#
# 主机: 127.0.0.1 (MySQL 8.0.30)
# 数据库: jingyu
# 生成时间: 2025-07-03 08:27:51 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# 转储表 account
# ------------------------------------------------------------

DROP TABLE IF EXISTS `account`;

CREATE TABLE `account` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '员工ID，主键，自增',
  `name` varchar(255) NOT NULL COMMENT '员工姓名，字符串类型，不能为空',
  `password` varchar(255) DEFAULT NULL COMMENT '员工密码，字符串类型',
  `create_at` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `login_time` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='账户表';

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;

INSERT INTO `account` (`id`, `name`, `password`, `create_at`, `create_by`, `login_time`)
VALUES
	(1,'admin','123456','2025-06-27 11:34:30','system',NULL);

/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 employee
# ------------------------------------------------------------

DROP TABLE IF EXISTS `employee`;

CREATE TABLE `employee` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '员工ID，主键，自增',
  `name` varchar(255) NOT NULL COMMENT '员工姓名',
  `avatar` varchar(500) DEFAULT NULL COMMENT '员工头像',
  `status` tinyint(1) DEFAULT '1' COMMENT '员工启停用状态',
  `on_board` tinyint(1) DEFAULT '1' COMMENT '员工在职状态',
  `is_authenticated` tinyint(1) DEFAULT '1' COMMENT '员工是否实名认证',
  `level` int DEFAULT '0' COMMENT '员工当前等级',
  `phone` varchar(20) DEFAULT NULL COMMENT '员工手机号码',
  `password` varchar(255) DEFAULT NULL COMMENT '员工密码',
  `salt` varchar(255) DEFAULT NULL COMMENT '密码加密盐',
  `nickname` varchar(255) DEFAULT NULL COMMENT '员工昵称',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '修改人',
  `points` int DEFAULT '0' COMMENT '积分',
  `total_work_time` decimal(10,2) DEFAULT '0.00' COMMENT '当前安全上工总时长',
  `current_month_time` decimal(10,2) DEFAULT '0.00' COMMENT '当月累计工时',
  `left_work_time` decimal(10,2) DEFAULT '0.00' COMMENT '剩余工时',
  `open_id` varchar(255) DEFAULT NULL COMMENT '微信openId',
  `onboarding_date` datetime DEFAULT NULL COMMENT '入职时间',
  `enable_date` datetime DEFAULT NULL COMMENT '启用时间',
  `disable_date` datetime DEFAULT NULL COMMENT '停用时间',
  `resign_date` datetime DEFAULT NULL COMMENT '离职时间',
  `audit_status` varchar(20) DEFAULT 'WAIT_AUDIT' COMMENT '审核状态',
  `reject_reason` text COMMENT '拒绝原因',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='员工表';

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;

INSERT INTO `employee` (`id`, `name`, `avatar`, `status`, `on_board`, `is_authenticated`, `level`, `phone`, `password`, `salt`, `nickname`, `create_time`, `create_by`, `update_time`, `update_by`, `points`, `total_work_time`, `current_month_time`, `left_work_time`, `open_id`, `onboarding_date`, `enable_date`, `disable_date`, `resign_date`, `audit_status`, `reject_reason`)
VALUES
	(1,'张三','https://example.com/avatar1.jpg',1,1,1,2,'13800138001','password123',NULL,'小张','2025-06-27 14:50:39',NULL,'2025-06-27 14:50:39',NULL,150,120.50,45.20,0.00,NULL,'2025-05-28 14:50:39','2025-06-26 14:50:39',NULL,NULL,'PASS',NULL),
	(2,'李四','https://example.com/avatar2.jpg',0,0,1,1,'13800138002','password123',NULL,'小李','2025-06-27 14:50:39',NULL,'2025-06-27 14:50:39',NULL,80,95.30,0.00,0.00,NULL,'2025-05-18 14:50:39','2025-06-17 14:50:39',NULL,NULL,'PASS',NULL),
	(3,'王五','https://example.com/avatar3.jpg',1,1,1,0,'13800138003','password123',NULL,'小王','2025-06-27 14:50:39',NULL,'2025-06-27 14:50:39',NULL,0,0.00,0.00,0.00,NULL,'2025-06-26 14:50:39','2025-06-26 14:50:39',NULL,NULL,'PASS',NULL),
	(4,'赵六','https://example.com/avatar4.jpg',1,1,1,3,'13800138004','password123',NULL,'小赵','2025-06-27 14:50:39',NULL,'2025-06-27 14:50:39',NULL,300,200.00,60.00,0.00,NULL,'2025-05-13 14:50:39','2025-06-12 14:50:39',NULL,NULL,'PASS',NULL),
	(5,'钱七','https://example.com/avatar5.jpg',1,1,1,3,'13800138005','password123',NULL,'小钱','2025-06-27 14:50:39',NULL,'2025-06-27 14:50:39',NULL,280,180.50,55.30,0.00,NULL,'2025-05-08 14:50:39','2025-06-07 14:50:39',NULL,NULL,'PASS',NULL),
	(6,'孙八','https://example.com/avatar6.jpg',1,1,1,2,'13800138006','password123',NULL,'小孙','2025-06-27 14:50:39',NULL,'2025-06-27 14:50:39',NULL,250,160.20,50.10,0.00,NULL,'2025-05-03 14:50:39','2025-06-02 14:50:39',NULL,NULL,'PASS',NULL),
	(7,'周九','https://example.com/avatar7.jpg',1,1,1,2,'13800138007','password123',NULL,'小周','2025-06-27 14:50:39',NULL,'2025-06-27 14:50:39',NULL,220,140.80,45.60,0.00,NULL,'2025-04-28 14:50:39','2025-05-28 14:50:39',NULL,NULL,'PASS',NULL),
	(8,'吴十','https://example.com/avatar8.jpg',1,1,1,1,'13800138008','password123',NULL,'小吴','2025-06-27 14:50:39',NULL,'2025-06-27 14:50:39',NULL,200,120.40,40.20,0.00,NULL,'2025-04-23 14:50:39','2025-05-23 14:50:39',NULL,NULL,'PASS',NULL),
	(9,'郑十一','https://example.com/avatar9.jpg',1,1,1,1,'13800138009','password123',NULL,'小郑','2025-06-27 14:50:39',NULL,'2025-06-27 14:50:39',NULL,180,100.60,35.80,0.00,NULL,'2025-04-18 14:50:39','2025-05-18 14:50:39',NULL,NULL,'PASS',NULL),
	(10,'王十二','https://example.com/avatar10.jpg',1,1,1,0,'13800138010','password123',NULL,'小王','2025-06-27 14:50:39',NULL,'2025-06-27 14:50:39',NULL,160,80.90,30.50,0.00,NULL,'2025-04-13 14:50:39','2025-05-13 14:50:39',NULL,NULL,'PASS',NULL),
	(11,'待审核员工1','https://example.com/avatar11.jpg',0,0,1,0,'13800138011','password123',NULL,'待审核1','2025-06-27 14:50:39',NULL,'2025-06-27 14:50:39',NULL,0,0.00,0.00,0.00,NULL,NULL,NULL,NULL,NULL,'WAIT_AUDIT',NULL),
	(12,'待审核员工2','https://example.com/avatar12.jpg',0,0,1,0,'13800138012','password123',NULL,'待审核2','2025-06-27 14:50:39',NULL,'2025-06-27 14:50:39',NULL,0,0.00,0.00,0.00,NULL,NULL,NULL,NULL,NULL,'WAIT_AUDIT',NULL),
	(13,'待审核员工3','https://example.com/avatar13.jpg',0,0,1,0,'13800138013','password123',NULL,'待审核3','2025-06-27 14:50:39',NULL,'2025-06-27 14:50:39',NULL,0,0.00,0.00,0.00,NULL,NULL,NULL,NULL,NULL,'WAIT_AUDIT',NULL);

/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 employee_attendance
# ------------------------------------------------------------

DROP TABLE IF EXISTS `employee_attendance`;

CREATE TABLE `employee_attendance` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `employee_id` int NOT NULL COMMENT '员工ID',
  `punch_date` date NOT NULL COMMENT '打卡日期',
  `common_punch_start` datetime DEFAULT NULL COMMENT '普通打卡开始时间',
  `common_punch_end` datetime DEFAULT NULL COMMENT '普通打卡结束时间',
  `common_work_shift_id` int DEFAULT NULL COMMENT '普通打卡班次ID',
  `common_punch_duration` decimal(10,2) DEFAULT '0.00' COMMENT '普通打卡时长',
  `over_punch_start` datetime DEFAULT NULL COMMENT '加班打卡开始时间',
  `over_punch_end` datetime DEFAULT NULL COMMENT '加班打卡结束时间',
  `over_punch_duration` decimal(10,2) DEFAULT '0.00' COMMENT '加班打卡总时长',
  `over_work_shift_id` int DEFAULT NULL COMMENT '加班打卡班次ID',
  `create_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_employee_id` (`employee_id`),
  KEY `idx_punch_date` (`punch_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='员工考勤表';

LOCK TABLES `employee_attendance` WRITE;
/*!40000 ALTER TABLE `employee_attendance` DISABLE KEYS */;

INSERT INTO `employee_attendance` (`id`, `employee_id`, `punch_date`, `common_punch_start`, `common_punch_end`, `common_work_shift_id`, `common_punch_duration`, `over_punch_start`, `over_punch_end`, `over_punch_duration`, `over_work_shift_id`, `create_at`)
VALUES
	(1,1,'2025-06-26','2025-06-26 09:00:00','2025-06-26 18:00:00',NULL,8.00,NULL,NULL,0.00,NULL,'2025-06-27 14:50:39'),
	(2,4,'2025-06-26','2025-06-26 08:00:00','2025-06-26 17:00:00',NULL,8.00,NULL,NULL,0.00,NULL,'2025-06-27 14:50:39'),
	(3,5,'2025-06-26','2025-06-26 08:30:00','2025-06-26 17:30:00',NULL,8.00,NULL,NULL,0.00,NULL,'2025-06-27 14:50:39'),
	(4,6,'2025-06-26','2025-06-26 09:00:00','2025-06-26 18:00:00',NULL,8.00,'2025-06-26 18:00:00','2025-06-26 21:00:00',2.00,NULL,'2025-06-27 14:50:39'),
	(5,7,'2025-06-26','2025-06-26 08:00:00','2025-06-26 17:00:00',NULL,8.00,'2025-06-26 17:00:00','2025-06-26 20:00:00',2.50,NULL,'2025-06-27 14:50:39'),
	(6,8,'2025-06-26','2025-06-26 08:30:00','2025-06-26 17:30:00',NULL,8.00,'2025-06-26 17:30:00','2025-06-26 22:00:00',3.00,NULL,'2025-06-27 14:50:39');

/*!40000 ALTER TABLE `employee_attendance` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 employee_details
# ------------------------------------------------------------

DROP TABLE IF EXISTS `employee_details`;

CREATE TABLE `employee_details` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '员工详情ID，主键，自增',
  `employee_id` int NOT NULL COMMENT '员工ID，关联employee表',
  `id_card` varchar(18) DEFAULT NULL COMMENT '员工身份证号码',
  `bank_card` varchar(20) DEFAULT NULL COMMENT '员工银行卡号码',
  `bank_branch` varchar(255) DEFAULT NULL COMMENT '员工银行卡所属支行信息',
  `bank_reserve_phone` varchar(20) DEFAULT NULL COMMENT '员工银行卡预留手机号码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '修改人',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `id_card_front_url` varchar(500) DEFAULT NULL COMMENT '身份证正面照片URL',
  `id_card_backend_url` varchar(500) DEFAULT NULL COMMENT '身份证背面照片URL',
  PRIMARY KEY (`id`),
  KEY `idx_employee_id` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='员工详细信息表';

LOCK TABLES `employee_details` WRITE;
/*!40000 ALTER TABLE `employee_details` DISABLE KEYS */;

INSERT INTO `employee_details` (`id`, `employee_id`, `id_card`, `bank_card`, `bank_branch`, `bank_reserve_phone`, `create_time`, `create_by`, `update_time`, `update_by`, `name`, `id_card_front_url`, `id_card_backend_url`)
VALUES
	(1,1,'330102199001011234','6222021234567890123','中国工商银行杭州分行','13800138001','2025-06-29 11:22:25','system','2025-06-29 11:22:25',NULL,'张三','idcard_front_1.jpg','idcard_back_1.jpg'),
	(2,2,'330102199002022345','6222021234567890124','中国建设银行杭州分行','13800138002','2025-06-29 11:22:25','system','2025-06-29 11:22:25',NULL,'李四','idcard_front_2.jpg','idcard_back_2.jpg'),
	(3,3,'330102199003033456','6222021234567890125','中国农业银行杭州分行','13800138003','2025-06-29 11:22:25','system','2025-06-29 11:22:25',NULL,'王五','idcard_front_3.jpg','idcard_back_3.jpg'),
	(4,4,'330102199004044567','6222021234567890126','中国银行杭州分行','13800138004','2025-06-29 11:22:25','system','2025-06-29 11:22:25',NULL,'赵六','idcard_front_4.jpg','idcard_back_4.jpg'),
	(5,5,'330102199005055678','6222021234567890127','招商银行杭州分行','13800138005','2025-06-29 11:22:25','system','2025-06-29 11:22:25',NULL,'钱七','idcard_front_5.jpg','idcard_back_5.jpg'),
	(6,6,'330102199006066789','6222021234567890128','浦发银行杭州分行','13800138006','2025-06-29 11:22:25','system','2025-06-29 11:22:25',NULL,'孙八','idcard_front_6.jpg','idcard_back_6.jpg'),
	(7,7,'330102199007077890','6222021234567890129','中信银行杭州分行','13800138007','2025-06-29 11:22:25','system','2025-06-29 11:22:25',NULL,'周九','idcard_front_7.jpg','idcard_back_7.jpg'),
	(8,8,'330102199008088901','6222021234567890130','民生银行杭州分行','13800138008','2025-06-29 11:22:25','system','2025-06-29 11:22:25',NULL,'吴十','idcard_front_8.jpg','idcard_back_8.jpg'),
	(9,9,'330102199009099012','6222021234567890131','兴业银行杭州分行','13800138009','2025-06-29 11:22:25','system','2025-06-29 11:22:25',NULL,'郑十一','idcard_front_9.jpg','idcard_back_9.jpg'),
	(10,10,'330102199010100123','6222021234567890132','平安银行杭州分行','13800138010','2025-06-29 11:22:25','system','2025-06-29 11:22:25',NULL,'王十二','idcard_front_10.jpg','idcard_back_10.jpg'),
	(11,11,'330102199011111234','6222021234567890133','华夏银行杭州分行','13800138011','2025-06-29 11:22:25','system','2025-06-29 11:22:25',NULL,'待审核员工1','idcard_front_11.jpg','idcard_back_11.jpg'),
	(12,12,'330102199012122345','6222021234567890134','广发银行杭州分行','13800138012','2025-06-29 11:22:25','system','2025-06-29 11:22:25',NULL,'待审核员工2','idcard_front_12.jpg','idcard_back_12.jpg'),
	(13,13,'330102199013133456','6222021234567890135','光大银行杭州分行','13800138013','2025-06-29 11:22:25','system','2025-06-29 11:22:25',NULL,'待审核员工3','idcard_front_13.jpg','idcard_back_13.jpg');

/*!40000 ALTER TABLE `employee_details` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 ent_action
# ------------------------------------------------------------

DROP TABLE IF EXISTS `ent_action`;

CREATE TABLE `ent_action` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '企业活动ID，主键，自增',
  `title` varchar(255) NOT NULL COMMENT '活动标题',
  `action_main_url` varchar(500) DEFAULT NULL COMMENT '活动主图URL',
  `html_content` text COMMENT '活动HTML内容',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `short_desc` varchar(500) DEFAULT NULL COMMENT '活动简短描述',
  `show_index` tinyint(1) DEFAULT '0' COMMENT '是否在首页展示',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='企业活动表';

LOCK TABLES `ent_action` WRITE;
/*!40000 ALTER TABLE `ent_action` DISABLE KEYS */;

INSERT INTO `ent_action` (`id`, `title`, `action_main_url`, `html_content`, `created_at`, `short_desc`, `show_index`)
VALUES
	(1,'2024年度员工表彰大会','action1.jpg','<h1>2024年度员工表彰大会</h1><p>表彰过去一年中表现优秀的员工，激励团队士气，共同展望美好未来。</p>','2025-06-27 17:45:16','年度盛典，表彰优秀员工',1),
	(2,'企业文化节','action2.jpg','<h1>企业文化节</h1><p>展示企业文化建设成果，增强员工归属感，提升团队凝聚力。</p>','2025-06-27 17:45:16','展示企业文化，增强团队凝聚力',1),
	(3,'技术创新大赛','action3.jpg','<h1>技术创新大赛</h1><p>激发员工创新潜能，展示技术实力，推动企业技术发展。</p>','2025-06-27 17:45:16','激发创新潜能，展示技术实力',1),
	(4,'新员工入职培训','action4.jpg','<h1>新员工入职培训</h1><p>帮助新员工快速融入团队，了解企业文化，掌握工作技能。</p>','2025-06-27 17:45:16','帮助新员工快速融入团队',0),
	(5,'团队建设活动','action5.jpg','<h1>团队建设活动</h1><p>通过户外拓展、团队游戏等方式，增强团队协作能力。</p>','2025-06-27 17:45:16','增强团队协作能力',0),
	(6,'技能提升培训','action6.jpg','<h1>技能提升培训</h1><p>定期组织专业技能培训，提升员工业务能力和综合素质。</p>','2025-06-27 17:45:16','提升员工业务能力和综合素质',0),
	(7,'健康体检活动','action7.jpg','<h1>健康体检活动</h1><p>关注员工身体健康，定期组织体检，提供健康保障。</p>','2025-06-27 17:45:16','关注员工身体健康',0),
	(8,'节日庆祝活动','action8.jpg','<h1>节日庆祝活动</h1><p>在重要节日组织庆祝活动，营造温馨的企业氛围。</p>','2025-06-27 17:45:16','营造温馨的企业氛围',0),
	(9,'公益活动','action9.jpg','<h1>公益活动</h1><p>组织员工参与社会公益活动，履行企业社会责任。</p>','2025-06-27 17:45:16','履行企业社会责任',0),
	(10,'知识分享会','action10.jpg','<h1>知识分享会</h1><p>定期组织知识分享活动，促进员工之间的学习交流。</p>','2025-06-27 17:45:16','促进员工之间的学习交流',0),
	(11,'产品发布会','action11.jpg','<h1>产品发布会</h1><p>发布新产品，展示企业创新成果，提升品牌影响力。</p>','2025-06-27 17:45:16','展示企业创新成果',0),
	(12,'客户答谢会','action12.jpg','<h1>客户答谢会</h1><p>感谢客户的支持与信任，加强客户关系维护。</p>','2025-06-27 17:45:16','加强客户关系维护',0),
	(13,'行业交流会','action13.jpg','<h1>行业交流会</h1><p>与行业同仁交流经验，了解行业发展趋势。</p>','2025-06-27 17:45:16','了解行业发展趋势',0),
	(14,'员工生日会','action14.jpg','<h1>员工生日会</h1><p>为员工庆祝生日，体现企业对员工的关怀。</p>','2025-06-27 17:45:16','体现企业对员工的关怀',0),
	(15,'年度总结大会','action15.jpg','<h1>年度总结大会</h1><p>总结过去一年的工作成果，规划新一年的发展目标。</p>','2025-06-27 17:45:16','总结成果，规划未来',0);

/*!40000 ALTER TABLE `ent_action` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 hzjy_config
# ------------------------------------------------------------

DROP TABLE IF EXISTS `hzjy_config`;

CREATE TABLE `hzjy_config` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `main_url` text COMMENT '主图URL配置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统配置表';

LOCK TABLES `hzjy_config` WRITE;
/*!40000 ALTER TABLE `hzjy_config` DISABLE KEYS */;

INSERT INTO `hzjy_config` (`id`, `main_url`)
VALUES
	(1,'[\"banner1.jpg\", \"banner2.jpg\", \"banner3.jpg\"]');

/*!40000 ALTER TABLE `hzjy_config` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 product
# ------------------------------------------------------------

DROP TABLE IF EXISTS `product`;

CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '产品ID，主键，自增',
  `title` varchar(255) NOT NULL COMMENT '产品标题',
  `product_main_url` varchar(500) DEFAULT NULL COMMENT '产品主图URL',
  `html_content` text COMMENT '产品HTML内容',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `short_desc` varchar(500) DEFAULT NULL COMMENT '产品简短描述',
  `show_index` tinyint(1) DEFAULT '0' COMMENT '是否在首页展示',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='产品表';

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;

INSERT INTO `product` (`id`, `title`, `product_main_url`, `html_content`, `created_at`, `short_desc`, `show_index`)
VALUES
	(1,'智能考勤系统','product1.jpg','<h1>智能考勤系统</h1><p>现代化的员工考勤管理解决方案，支持多种打卡方式，实时数据统计。</p>','2025-06-27 17:21:12','企业级考勤管理平台，提升管理效率',1),
	(2,'员工管理系统','product2.jpg','<h1>员工管理系统</h1><p>全面的员工信息管理，包括入职、离职、调岗等全生命周期管理。</p>','2025-06-27 17:21:12','一站式员工信息管理解决方案',1),
	(3,'数据分析平台','product3.jpg','<h1>数据分析平台</h1><p>基于大数据技术的企业数据分析平台，提供深度洞察和决策支持。</p>','2025-06-27 17:21:12','智能数据分析，助力企业决策',1),
	(4,'移动端APP','product4.jpg','<h1>移动端APP</h1><p>支持iOS和Android的移动端应用，随时随地处理工作事务。</p>','2025-06-27 17:21:12','移动办公，随时随地',0),
	(5,'考勤报表系统','product5.jpg','<h1>考勤报表系统</h1><p>自动生成各类考勤报表，支持自定义报表模板和导出功能。</p>','2025-06-27 17:21:12','自动化报表生成，数据一目了然',0),
	(6,'权限管理系统','product6.jpg','<h1>权限管理系统</h1><p>细粒度的权限控制，确保数据安全和系统稳定运行。</p>','2025-06-27 17:21:12','安全可靠的权限管理',0),
	(7,'工作流引擎','product7.jpg','<h1>工作流引擎</h1><p>灵活的工作流配置，支持复杂的业务流程自动化处理。</p>','2025-06-27 17:21:12','业务流程自动化处理',0),
	(8,'消息通知系统','product8.jpg','<h1>消息通知系统</h1><p>多渠道消息推送，包括短信、邮件、APP推送等多种通知方式。</p>','2025-06-27 17:21:12','多渠道消息推送服务',0),
	(9,'API接口服务','product9.jpg','<h1>API接口服务</h1><p>提供标准化的RESTful API接口，支持第三方系统集成。</p>','2025-06-27 17:21:12','标准化API接口，易于集成',0),
	(10,'数据备份服务','product10.jpg','<h1>数据备份服务</h1><p>自动化的数据备份和恢复服务，确保数据安全和业务连续性。</p>','2025-06-27 17:21:12','数据安全备份，业务连续性保障',0),
	(11,'系统监控平台','product11.jpg','<h1>系统监控平台</h1><p>实时监控系统运行状态，及时发现和处理系统异常。</p>','2025-06-27 17:21:12','实时系统监控，异常及时处理',0),
	(12,'用户行为分析','product12.jpg','<h1>用户行为分析</h1><p>深度分析用户使用行为，为产品优化提供数据支持。</p>','2025-06-27 17:21:12','用户行为深度分析',0),
	(13,'多语言支持','product13.jpg','<h1>多语言支持</h1><p>支持多种语言界面，满足国际化业务需求。</p>','2025-06-27 17:21:12','国际化多语言支持',0),
	(14,'云部署方案','product14.jpg','<h1>云部署方案</h1><p>支持公有云、私有云、混合云等多种部署方式。</p>','2025-06-27 17:21:12','灵活的云部署方案',0),
	(15,'技术支持服务','product15.jpg','<h1>技术支持服务</h1><p>7x24小时专业技术支持，确保系统稳定运行。</p>','2025-06-27 17:21:12','专业的技术支持服务',0);

/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
