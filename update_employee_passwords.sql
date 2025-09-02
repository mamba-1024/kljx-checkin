-- 为现有员工设置默认密码的SQL脚本
-- 注意：这只是为了测试，生产环境应该让员工自己设置密码

-- 更新员工密码为默认密码 "123456"
-- 这里使用明文密码，实际使用时应该使用加密后的密码
UPDATE employee 
SET password = '123456', 
    salt = NULL 
WHERE password IS NULL OR password = '';

-- 查看更新结果
SELECT id, name, phone, password, salt FROM employee WHERE password IS NOT NULL;

-- 为测试方便，也可以设置一些特定的测试账号
-- 例如：为张三设置密码为 "123456"
-- UPDATE employee SET password = '123456', salt = NULL WHERE name = '张三';

-- 查看所有员工信息
SELECT id, name, phone, password, salt, status, on_board, audit_status FROM employee; 