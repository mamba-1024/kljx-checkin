-- 创建EntAction表并初始化数据
-- 对应BackendEntActionController中的/list接口

-- 1. 创建ent_action表
CREATE TABLE IF NOT EXISTS `ent_action` (
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '企业活动ID，主键，自增',
    `title` VARCHAR(255) NOT NULL COMMENT '活动标题',
    `action_main_url` VARCHAR(500) COMMENT '活动主图URL',
    `html_content` TEXT COMMENT '活动HTML内容',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `short_desc` VARCHAR(500) COMMENT '活动简短描述',
    `show_index` BOOLEAN DEFAULT FALSE COMMENT '是否在首页展示'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业活动表';

-- 2. 初始化15条企业活动数据
INSERT INTO `ent_action` (
    `title`, `action_main_url`, `html_content`, `short_desc`, `show_index`
) VALUES 
-- 首页展示活动
('2024年度员工表彰大会', 'action1.jpg', 
 '<h1>2024年度员工表彰大会</h1><p>表彰过去一年中表现优秀的员工，激励团队士气，共同展望美好未来。</p>', 
 '年度盛典，表彰优秀员工', TRUE),

('企业文化节', 'action2.jpg', 
 '<h1>企业文化节</h1><p>展示企业文化建设成果，增强员工归属感，提升团队凝聚力。</p>', 
 '展示企业文化，增强团队凝聚力', TRUE),

('技术创新大赛', 'action3.jpg', 
 '<h1>技术创新大赛</h1><p>激发员工创新潜能，展示技术实力，推动企业技术发展。</p>', 
 '激发创新潜能，展示技术实力', TRUE),

-- 普通活动
('新员工入职培训', 'action4.jpg', 
 '<h1>新员工入职培训</h1><p>帮助新员工快速融入团队，了解企业文化，掌握工作技能。</p>', 
 '帮助新员工快速融入团队', FALSE),

('团队建设活动', 'action5.jpg', 
 '<h1>团队建设活动</h1><p>通过户外拓展、团队游戏等方式，增强团队协作能力。</p>', 
 '增强团队协作能力', FALSE),

('技能提升培训', 'action6.jpg', 
 '<h1>技能提升培训</h1><p>定期组织专业技能培训，提升员工业务能力和综合素质。</p>', 
 '提升员工业务能力和综合素质', FALSE),

('健康体检活动', 'action7.jpg', 
 '<h1>健康体检活动</h1><p>关注员工身体健康，定期组织体检，提供健康保障。</p>', 
 '关注员工身体健康', FALSE),

('节日庆祝活动', 'action8.jpg', 
 '<h1>节日庆祝活动</h1><p>在重要节日组织庆祝活动，营造温馨的企业氛围。</p>', 
 '营造温馨的企业氛围', FALSE),

('公益活动', 'action9.jpg', 
 '<h1>公益活动</h1><p>组织员工参与社会公益活动，履行企业社会责任。</p>', 
 '履行企业社会责任', FALSE),

('知识分享会', 'action10.jpg', 
 '<h1>知识分享会</h1><p>定期组织知识分享活动，促进员工之间的学习交流。</p>', 
 '促进员工之间的学习交流', FALSE),

('产品发布会', 'action11.jpg', 
 '<h1>产品发布会</h1><p>发布新产品，展示企业创新成果，提升品牌影响力。</p>', 
 '展示企业创新成果', FALSE),

('客户答谢会', 'action12.jpg', 
 '<h1>客户答谢会</h1><p>感谢客户的支持与信任，加强客户关系维护。</p>', 
 '加强客户关系维护', FALSE),

('行业交流会', 'action13.jpg', 
 '<h1>行业交流会</h1><p>与行业同仁交流经验，了解行业发展趋势。</p>', 
 '了解行业发展趋势', FALSE),

('员工生日会', 'action14.jpg', 
 '<h1>员工生日会</h1><p>为员工庆祝生日，体现企业对员工的关怀。</p>', 
 '体现企业对员工的关怀', FALSE),

('年度总结大会', 'action15.jpg', 
 '<h1>年度总结大会</h1><p>总结过去一年的工作成果，规划新一年的发展目标。</p>', 
 '总结成果，规划未来', FALSE);

-- 3. 验证数据
-- 查看所有企业活动
SELECT * FROM ent_action;

-- 查看首页展示的活动
SELECT * FROM ent_action WHERE show_index = TRUE;

-- 查看活动数量统计
SELECT 
    COUNT(*) AS total_count,
    COUNT(CASE WHEN show_index = TRUE THEN 1 END) AS show_index_count,
    COUNT(CASE WHEN show_index = FALSE THEN 1 END) AS hide_index_count
FROM ent_action;

-- 按创建时间排序查看活动
SELECT id, title, created_at, show_index 
FROM ent_action 
ORDER BY created_at DESC; 