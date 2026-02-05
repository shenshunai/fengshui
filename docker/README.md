# 平台 Docker 一键部署

本目录包含**全栈平台**的 Docker Compose 编排，支持一键启动：网关、Nacos、MySQL、Redis、RabbitMQ、Kafka、玄学微服务、购物微服务（占位）、AI 服务、前端。

## 前置要求

- Docker 与 Docker Compose 已安装
- 如需 AI 能力，在 `docker/.env` 中配置 `DEEPSEEK_API_KEY` 或 `OPENAI_API_KEY`

## 一键启动

**Windows（项目根目录执行）：**
```bat
deploy.bat
```

**Linux / macOS：**
```bash
chmod +x deploy.sh
./deploy.sh
```

或手动进入 `docker` 目录执行：
```bash
cd docker
cp .env.example .env   # 首次需复制并按需修改
docker-compose up -d --build
```

## 服务与端口

| 服务 | 端口 | 说明 |
|------|------|------|
| 前端 (Nginx) | 80 | 静态资源 + /api 代理到网关 |
| 网关 (Gateway) | 8080 | 统一入口，路由到各微服务 |
| Nacos | 8848, 9848, 9849 | 注册中心 + 配置中心，控制台 /nacos |
| MySQL | 3306 | 业务库 xuanxue |
| Redis | 6379 | 缓存 |
| RabbitMQ | 5672, 15672 | 消息队列，管理端 15672 |
| Kafka | 9092 | 消息队列 |
| 玄学服务 | 内网 | 注册到 Nacos，由网关转发 |
| 购物服务 | 内网 | 占位微服务 |
| AI 服务 | 9000 | Python 大模型接口 |

## 访问

- **平台首页**：http://localhost  
- **Nacos 控制台**：http://localhost:8848/nacos（账号/密码：nacos/nacos）  
- **RabbitMQ 管理**：http://localhost:15672（默认 admin/admin123）

## 环境变量

在 `docker/.env` 中配置（可从 `.env.example` 复制）：

- `NACOS_*`：Nacos 连接与鉴权
- `MYSQL_*`：MySQL 库名与账号
- `RABBITMQ_DEFAULT_USER/PASS`：RabbitMQ 管理账号
- `DEEPSEEK_API_KEY` / `OPENAI_API_KEY`：AI 服务 Key
- `JWT_SECRET`：玄学服务 JWT 密钥

## 停止与清理

```bash
cd docker
docker-compose down
# 需清空数据卷时：docker-compose down -v
```

## 新增微服务

1. 在 `services/` 下新建 Spring Boot 模块，接入 Nacos Discovery，注册名如 `xxx-service`。
2. 在 `docker/docker-compose.yml` 中增加该服务的 build 与 environment（`NACOS_SERVER_ADDR` 等）。
3. 在 `gateway/src/main/resources/application.yml` 中增加路由，例如 `/api/xxx/**` -> `lb://xxx-service`。
4. 如需新 Dockerfile，在 `docker/` 下增加 `Dockerfile.xxx`，build 的 context 指向该模块所在目录。
