-- 创建Product表并初始化数据
-- 对应BackendProductController中的/list接口

-- 1. 创建product表
CREATE TABLE IF NOT EXISTS `product` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '产品ID，主键，自增',
    `title` VARCHAR(255) NOT NULL COMMENT '产品标题',
    `product_main_url` VARCHAR(500) COMMENT '产品主图URL',
    `html_content` TEXT COMMENT '产品HTML内容',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `short_desc` VARCHAR(500) COMMENT '产品简短描述',
    `show_index` BOOLEAN DEFAULT FALSE COMMENT '是否在首页展示'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品表';

-- 2. 初始化15条产品数据
INSERT INTO `product` (
    `title`, `product_main_url`, `html_content`, `short_desc`, `show_index`
) VALUES 
-- 首页展示产品
('智能考勤系统', 'product1.jpg', 
 '<h1>智能考勤系统</h1><p>现代化的员工考勤管理解决方案，支持多种打卡方式，实时数据统计。</p>', 
 '企业级考勤管理平台，提升管理效率', TRUE),

('员工管理系统', 'product2.jpg', 
 '<h1>员工管理系统</h1><p>全面的员工信息管理，包括入职、离职、调岗等全生命周期管理。</p>', 
 '一站式员工信息管理解决方案', TRUE),

('数据分析平台', 'product3.jpg', 
 '<h1>数据分析平台</h1><p>基于大数据技术的企业数据分析平台，提供深度洞察和决策支持。</p>', 
 '智能数据分析，助力企业决策', TRUE),

-- 普通产品
('移动端APP', 'product4.jpg', 
 '<h1>移动端APP</h1><p>支持iOS和Android的移动端应用，随时随地处理工作事务。</p>', 
 '移动办公，随时随地', FALSE),

('考勤报表系统', 'product5.jpg', 
 '<h1>考勤报表系统</h1><p>自动生成各类考勤报表，支持自定义报表模板和导出功能。</p>', 
 '自动化报表生成，数据一目了然', FALSE),

('权限管理系统', 'product6.jpg', 
 '<h1>权限管理系统</h1><p>细粒度的权限控制，确保数据安全和系统稳定运行。</p>', 
 '安全可靠的权限管理', FALSE),

('工作流引擎', 'product7.jpg', 
 '<h1>工作流引擎</h1><p>灵活的工作流配置，支持复杂的业务流程自动化处理。</p>', 
 '业务流程自动化处理', FALSE),

('消息通知系统', 'product8.jpg', 
 '<h1>消息通知系统</h1><p>多渠道消息推送，包括短信、邮件、APP推送等多种通知方式。</p>', 
 '多渠道消息推送服务', FALSE),

('API接口服务', 'product9.jpg', 
 '<h1>API接口服务</h1><p>提供标准化的RESTful API接口，支持第三方系统集成。</p>', 
 '标准化API接口，易于集成', FALSE),

('数据备份服务', 'product10.jpg', 
 '<h1>数据备份服务</h1><p>自动化的数据备份和恢复服务，确保数据安全和业务连续性。</p>', 
 '数据安全备份，业务连续性保障', FALSE),

('系统监控平台', 'product11.jpg', 
 '<h1>系统监控平台</h1><p>实时监控系统运行状态，及时发现和处理系统异常。</p>', 
 '实时系统监控，异常及时处理', FALSE),

('用户行为分析', 'product12.jpg', 
 '<h1>用户行为分析</h1><p>深度分析用户使用行为，为产品优化提供数据支持。</p>', 
 '用户行为深度分析', FALSE),

('多语言支持', 'product13.jpg', 
 '<h1>多语言支持</h1><p>支持多种语言界面，满足国际化业务需求。</p>', 
 '国际化多语言支持', FALSE),

('云部署方案', 'product14.jpg', 
 '<h1>云部署方案</h1><p>支持公有云、私有云、混合云等多种部署方式。</p>', 
 '灵活的云部署方案', FALSE),

('技术支持服务', 'product15.jpg', 
 '<h1>技术支持服务</h1><p>7x24小时专业技术支持，确保系统稳定运行。</p>', 
 '专业的技术支持服务', FALSE);

-- 3. 验证数据
-- 查看所有产品
SELECT * FROM product;

-- 查看首页展示的产品
SELECT * FROM product WHERE show_index = TRUE;

-- 查看产品数量统计
SELECT 
    COUNT(*) AS total_count,
    COUNT(CASE WHEN show_index = TRUE THEN 1 END) AS show_index_count,
    COUNT(CASE WHEN show_index = FALSE THEN 1 END) AS hide_index_count
FROM product; 