# 玄学应用平台

一个集星座运势、八字算命、风水测算、起名服务、抽签占卜于一体的现代化玄学应用平台。

## 功能特性

- ⭐ **星座运势** - 每日/每周/每月运势分析
- ☯ **八字排盘** - 精准八字命理分析
- 🏠 **风水测算** - 家居风水布局分析
- 📝 **起名服务** - AI智能起名推荐
- 🎴 **抽签占卜** - 观音灵签、月老签等

## 技术栈

### 后端
- Spring Boot 3.2
- Spring Security + JWT
- Spring Data JPA
- MySQL 8 / H2 (开发)
- Redis

### 前端
- Vue 3 + TypeScript
- Vite 5
- Element Plus
- Pinia
- Vue Router

## 快速开始

### 环境要求
- JDK 17+
- Node.js 18+
- Maven 3.8+

### 后端启动

```bash
cd backend
mvn spring-boot:run
```

访问:
- API文档: http://localhost:8080/swagger-ui.html
- H2控制台: http://localhost:8080/h2-console

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

访问: http://localhost:5173

### Docker部署

```bash
cd docker
docker-compose up -d
```

## API示例

### 八字计算
```bash
curl -X GET "http://localhost:8080/api/bazi/test?year=1990&month=5&day=15&hour=10"
```

### 星座运势
```bash
curl -X GET "http://localhost:8080/api/zodiac/fortune/today/白羊座"
```

### 用户注册
```bash
curl -X POST "http://localhost:8080/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"phone":"13800138000","password":"123456"}'
```

### 用户登录
```bash
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"phone":"13800138000","password":"123456"}'
```

## 项目结构

```
xuanxue-app/
├── backend/                 # Spring Boot后端
│   ├── src/main/java/      # Java源码
│   └── src/main/resources/ # 配置文件
├── frontend/               # Vue 3前端
│   ├── src/views/          # 页面组件
│   └── src/router/         # 路由配置
├── docker/                 # Docker配置
│   ├── docker-compose.yml  # 编排文件
│   └── init.sql           # 数据库初始化
└── docs/                   # 项目文档
```

## 核心算法

### 八字计算
- 年柱：(年份-4) % 60
- 月柱：五虎遁推算
- 日柱：以1900-01-01为基准
- 时柱：五鼠遁推算

### 星座判断
根据出生月日判断所属星座，支持跨年处理（摩羯座）。

## 贡献指南

1. Fork 本项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 提交 Pull Request

## 许可证

MIT License

## 更新日志

### v1.0.0 (2024-01)
- 初始版本发布
- 实现八字排盘功能
- 实现星座运势功能
- 实现抽签占卜功能
- 用户认证系统
