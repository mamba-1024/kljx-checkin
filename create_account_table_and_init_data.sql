-- 创建account表并初始化管理员数据
-- 根据Account.java实体类结构创建

-- 1. 创建account表
CREATE TABLE IF NOT EXISTS `account` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '员工ID，主键，自增',
    `name` VARCHAR(255) NOT NULL COMMENT '员工姓名，字符串类型，不能为空',
    `password` VARCHAR(255) COMMENT '员工密码，字符串类型',
    `create_at` DATETIME COMMENT '创建时间',
    `create_by` VARCHAR(255) COMMENT '创建者',
    `login_time` DATETIME COMMENT '登录时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户表';

-- 2. 初始化管理员账户数据
INSERT INTO `account` (
    `name`, 
    `password`, 
    `create_at`, 
    `create_by`, 
    `login_time`
) VALUES (
    'admin',           -- 用户名
    '123456',          -- 密码
    NOW(),             -- 创建时间
    'system',          -- 创建者
    NULL               -- 登录时间（初始为空）
);

-- 3. 验证表结构和数据
-- 查看表结构
DESCRIBE `account`;

-- 查看插入的数据
SELECT * FROM `account` WHERE `name` = 'admin';

-- 4. 可选：创建索引以提高查询性能
CREATE INDEX `idx_account_name` ON `account` (`name`);