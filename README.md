# 二手交易市场后端

## 项目介绍
本项目是一个二手交易市场的后端系统，基于Spring Boot构建，提供用户注册、登录、商品发布、订单管理等功能的API服务。

## 技术栈
- Java 23
- Spring Boot 3.2.3
- Spring Data JPA
- MyBatis
- MySQL
- Redis
- Alibaba OSS
- Spring Validation
- Lombok

## 主要功能
- 用户管理：注册、登录、个人信息修改
- 商品管理：发布、查询、修改、删除
- 订单管理：创建、支付、取消
- 评论与回复
- 商品点赞
- 聊天功能

## 项目结构
```
src/main/java/org/example/market_backend/
├── BO           // 业务对象
├── Constants    // 常量定义
├── Entity       // 实体类
├── Exception    // 自定义异常
├── Mapper       // 数据访问层
├── Service      // 业务逻辑层
├── Utils        // 工具类
├── VO           // 视图对象
└── controller   // 控制器
```

## 配置与部署
1. 克隆项目：`git clone https://github.com/Sh1tFXXK/market_backend.git`
2. 配置数据库连接（application.properties）
3. 配置Redis连接
4. 配置阿里云OSS（如需使用文件存储）
5. 运行应用：`mvn spring-boot:run`

## API文档
API文档通过Swagger生成，启动应用后访问：`http://localhost:8080/swagger-ui.html`

## 数据库设计
项目包含以下主要数据表：
- user：用户信息
- product：商品信息
- product_type：商品类型
- order：订单信息
- address：收货地址
- chat/chat_detail：聊天相关
- comment_reply：评论回复
- praise：点赞信息