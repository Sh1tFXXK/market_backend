# 二手市场后端部署指南

本文档提供了如何部署和运行二手市场后端服务的详细说明。

## 系统要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- Nginx (可选，用于反向代理)

## 部署步骤

### 1. 准备数据库

1. 创建MySQL数据库并导入初始化脚本：

```bash
mysql -u root -p < sql/market_backend.sql
```

2. 确保Redis服务已启动：

```bash
# Ubuntu/Debian
sudo systemctl start redis-server

# CentOS
sudo systemctl start redis

# Windows
# 启动Redis服务
```

### 2. 配置应用

1. 修改`src/main/resources/application.properties`文件，配置数据库连接信息：

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/market_backend?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
spring.datasource.username=your_username
spring.datasource.password=your_password

# Redis配置
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.password=your_redis_password
```

2. 配置阿里云OSS（如需使用）：

修改`src/main/java/org/example/market_backend/Utils/OssUtil.java`文件中的OSS配置：

```java
public static OSS getOssClient() {
    return new OSSClientBuilder().build("your-endpoint", "your-access-key-id", "your-access-key-secret");
}

public static String getBucketName() {
    return "your-bucket-name";
}

public static String getOssUrl() {
    return "https://your-bucket-name.your-region.oss.aliyun.com";
}
```

3. 配置阿里云短信服务（如需使用）：

修改`src/main/java/org/example/market_backend/Utils/AliSmsUtils.java`文件中的短信配置。

### 3. 构建应用

使用Maven构建应用：

```bash
mvn clean package -DskipTests
```

构建成功后，将在`target`目录下生成JAR文件。

### 4. 运行应用

#### 方式一：直接使用Java命令

```bash
java -jar target/market_backend-0.0.1-SNAPSHOT.jar
```

#### 方式二：使用Spring Boot Maven插件

```bash
mvn spring-boot:run
```

#### 方式三：使用Docker（需要额外配置Dockerfile）

```bash
docker build -t market_backend .
docker run -p 8080:8080 market_backend
```

### 5. 验证部署

访问以下URL验证应用是否成功启动：

```
http://localhost:8080/
```

如果返回欢迎信息，说明应用已成功部署。

## 常见问题

### 数据库连接问题

- 确保MySQL服务正在运行
- 验证数据库用户名和密码是否正确
- 检查数据库是否允许远程连接（如果应用和数据库不在同一服务器）

### Redis连接问题

- 确保Redis服务正在运行
- 验证Redis密码是否正确
- 检查Redis是否允许远程连接（如果应用和Redis不在同一服务器）

### 应用启动失败

- 检查日志文件，查看具体错误
- 确认所有依赖项是否正确安装
- 验证JDK版本是否满足要求

## 生产环境部署建议

1. **使用HTTPS**: 在生产环境中，强烈建议使用HTTPS保护API通信。
2. **配置服务监控**: 使用Spring Boot Actuator或其他监控工具监控服务状态。
3. **日志管理**: 配置适当的日志级别和轮换策略，避免日志文件占用过多磁盘空间。
4. **使用连接池**: 确保数据库和Redis连接使用连接池，优化性能。
5. **配置防火墙**: 限制只允许必要的端口访问。
6. **定期备份**: 设置定期备份数据库，防止数据丢失。

## 参考资源

- [Spring Boot官方文档](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [MySQL官方文档](https://dev.mysql.com/doc/)
- [Redis官方文档](https://redis.io/documentation)