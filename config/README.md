# 配置文件说明

## 敏感信息管理

为了安全起见，敏感信息（如数据库密码、API密钥等）已从主配置文件中分离出来，存储在本地配置文件中。

### 文件结构

```
config/
├── application-local.yml          # 本地配置文件（包含敏感信息，不提交到git）
├── application-local-template.yml  # 配置文件模板
└── README.md                      # 本说明文档
```

### 使用步骤

1. **复制模板文件**：
   ```bash
   cp config/application-local-template.yml config/application-local.yml
   ```

2. **编辑配置文件**：
   在 `config/application-local.yml` 中填入实际的敏感信息：
   ```yaml
   spring:
     datasource:
       username: your_actual_username
       password: your_actual_password
   
   wx:
     appSecret: your_actual_wechat_secret
     appId: your_actual_wechat_app_id
   ```

3. **启动应用**：
   应用会自动加载 `dev` 和 `local` 两个配置文件。

### 安全注意事项

- ✅ `config/application-local.yml` 已被 `.gitignore` 过滤，不会提交到git
- ✅ 敏感信息只存储在本地，不会上传到代码仓库
- ✅ 团队成员需要各自配置自己的本地配置文件
- ⚠️ 请勿将 `application-local.yml` 文件分享给他人
- ⚠️ 定期更新密码和密钥

### 配置项说明

| 配置项 | 说明 | 示例 |
|--------|------|------|
| `spring.datasource.username` | 数据库用户名 | `program` |
| `spring.datasource.password` | 数据库密码 | `your_password` |
| `wx.appSecret` | 微信小程序密钥 | `your_wechat_secret` |
| `wx.appId` | 微信小程序ID | `your_wechat_app_id` |
| `bank.verify.appCode` | 银行验证API密钥 | `your_bank_app_code` |

### 故障排除

如果遇到配置加载问题，请检查：

1. 文件路径是否正确：`config/application-local.yml`
2. YAML格式是否正确（注意缩进）
3. 应用启动参数是否正确：`spring.profiles.active=dev,local`
