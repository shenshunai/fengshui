# 玄学与多业务 Web 平台

基于 Spring Cloud 的**统一 Web 平台**：网关 + Nacos + 多微服务 + AI 服务 + 前端，支持一键 Docker 部署。

## 架构概览

```
                    ┌─────────────┐
                    │   浏览器     │
                    └──────┬──────┘
                           │ :80
                    ┌──────▼──────┐
                    │   Nginx     │  静态资源 + /api 反向代理
                    │  (frontend) │
                    └──────┬──────┘
                           │ /api -> :8080
                    ┌──────▼──────┐
                    │   Gateway   │  Spring Cloud Gateway
                    │   :8080     │  路由、CORS、负载均衡
                    └──────┬──────┘
           ┌───────────────┼───────────────┐
           │               │               │
    ┌──────▼──────┐ ┌──────▼──────┐ ┌─────▼─────┐
    │ xuanxue-    │ │ shopping-   │ │   Nacos   │
    │ service     │ │ service     │ │  :8848    │
    │ (玄学)      │ │ (购物占位)  │ │ 注册/配置  │
    └──────┬──────┘ └─────────────┘ └──────────┘
           │
    ┌──────▼──────┐
    │ ai-service  │  Python / DeepSeek or OpenAI
    │   :9000     │
    └─────────────┘

    中间件：MySQL :3306 | Redis :6379 | RabbitMQ :5672/15672 | Kafka :9092
```

## 目录结构

```
fengshui/
├── docker/                    # 平台一键 Docker 编排
│   ├── docker-compose.yml     # 全栈：网关、Nacos、中间件、各服务
│   ├── .env.example / .env
│   ├── nginx-platform.conf
│   ├── Dockerfile.gateway
│   ├── Dockerfile.shopping
│   └── README.md
├── gateway/                   # Spring Cloud Gateway
│   ├── pom.xml
│   └── src/...
├── services/
│   └── shopping-service/      # 购物微服务（占位，可扩展）
├── xuanxue-app/
│   ├── backend/               # 玄学微服务（Nacos 注册名 xuanxue-service）
│   ├── frontend/              # Vue 前端
│   ├── ai-service/            # Python AI（起名、星座、风水）
│   └── docker/                # 原有单应用部署（可选）
├── deploy.bat                 # Windows 一键部署
├── deploy.sh                  # Linux/macOS 一键部署
└── README-PLATFORM.md          # 本说明
```

## 技术栈

| 类别 | 技术 |
|------|------|
| 网关 | Spring Cloud Gateway |
| 注册/配置中心 | Nacos |
| 微服务 | Spring Boot 2.7、Spring Cloud 2021、Nacos Discovery/Config |
| 前端 | Vue 3、Vite、Element Plus |
| AI | Python、FastAPI、DeepSeek / OpenAI |
| 中间件 | MySQL 8、Redis 7、RabbitMQ、Kafka |
| 部署 | Docker、Docker Compose |

## 快速开始

### 一键 Docker 部署（推荐）

在项目根目录执行：

- **Windows**：`deploy.bat`
- **Linux/macOS**：`./deploy.sh`

首次会从 `docker/.env.example` 生成 `docker/.env`，可按需修改（如 `DEEPSEEK_API_KEY`、数据库密码等）后再次执行。

访问：**http://localhost**（前端），**http://localhost:8848/nacos**（Nacos 控制台，nacos/nacos）。

### 本地开发（不跑 Docker）

1. **启动 Nacos**（可选，不用则网关与微服务可单机直连）：  
   `docker run -d -p 8848:8848 -p 9848:9848 -p 9849:9849 nacos/nacos-server:v2.3.0 -e MODE=standalone`
2. **中间件**：本地安装或 Docker 单独启动 MySQL、Redis 等。
3. **玄学后端**：`cd xuanxue-app/backend && mvn spring-boot:run`（默认不启用 Nacos，可设 `NACOS_DISCOVERY_ENABLED=true` 并保证 Nacos 地址正确）。
4. **网关**：`cd gateway && mvn spring-boot:run`（需 Nacos 中有 xuanxue-service）。
5. **AI 服务**：`cd xuanxue-app/ai-service && python main.py`。
6. **前端**：`cd xuanxue-app/frontend && npm run dev`。开发时可将代理指向后端直连或网关。

## 新增微服务步骤

1. 在 `services/` 下新建 Spring Boot 项目，引入 `spring-cloud-starter-alibaba-nacos-discovery`（及可选 `nacos-config`、`spring-cloud-starter-bootstrap`），在 `bootstrap.yml` 中配置 Nacos 地址，`spring.application.name` 设为服务名（如 `order-service`）。
2. 在 `docker/docker-compose.yml` 中增加该服务的 build、environment（如 `NACOS_SERVER_ADDR`）、depends_on（如 nacos）。
3. 在 `gateway/src/main/resources/application.yml` 的 `spring.cloud.gateway.routes` 中增加一条路由，例如 `/api/order/**` -> `lb://order-service`，并视需求加 RewritePath。
4. 在 `docker/` 下为该服务添加 `Dockerfile.xxx`（若与现有模板不同），并在 compose 中指定 `dockerfile` 与 `context`。

按上述步骤即可在现有平台上快速扩展新业务（如订单、支付、营销等）。

## 补充说明

- **配置中心**：各服务可通过 Nacos Config 拉取公共配置或独立 DataId，便于多环境与密钥管理。
- **消息队列**：Redis/RabbitMQ/Kafka 已就绪，新微服务可按需引入 Spring AMQP 或 Spring Kafka。
- **监控与链路**：后续可接入 Spring Boot Admin、Sentinel、SkyWalking 等，只需增加依赖与 Docker 服务即可。

详细部署与端口说明见 **docker/README.md**。
