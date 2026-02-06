# SSA-Project

请将本仓库根目录**文件夹名**从 `fengshui` 重命名为 **`SSA-Project`**（若尚未重命名）。

## 目录结构

```
SSA-Project/
├── gateway/           # 网关服务
├── auth-service/      # 鉴权登录服务
├── shopping-service/  # 购物服务（占位）
├── xuanxue-service/   # 玄学服务
├── ai-service/        # AI 服务（Python）
├── frontend/          # 前端
├── common-service/    # 公共模块（Result、JWT、异常等）
├── sql/               # 建表语句（统一放此处）
│   └── init.sql       # MySQL 全量初始化
├── docker/            # Docker 编排与 Dockerfile
├── pom.xml            # Maven 聚合（common-service、gateway、auth、shopping、xuanxue）
├── deploy.bat         # Windows 一键部署
└── deploy.sh          # Linux/macOS 一键部署
```

## 构建

```bash
mvn clean install -DskipTests
```

## 一键 Docker 部署

在项目根目录执行：`deploy.bat`（Windows）或 `./deploy.sh`（Linux/macOS）。  
详见 `docker/README.md`。

## 建表脚本

所有建表语句集中在 **`sql/`** 目录，生产与 Docker 使用 `sql/init.sql`。

## 保持仓库干净

- 已配置根目录 **`.gitignore`**（忽略 `target/`、`node_modules/`、`.env`、`.vite/`、`__pycache__/` 等）。
- 清理 Maven 构建产物：在根目录执行 `mvn clean`。
- 若仍存在残留目录 `xuanxue-app`（因占用未删掉），关闭 IDE 后手动删除即可。
