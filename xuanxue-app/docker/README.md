# Docker 部署说明

## 🚀 快速开始

### Windows 用户
双击运行 `快速部署.bat` 文件，或在命令行执行：
```bash
快速部署.bat
```

### Linux/Mac 用户
```bash
chmod +x 快速部署.sh
./快速部署.sh
```

### 手动部署
```bash
# 进入docker目录
cd docker

# 启动所有服务
docker-compose up -d --build

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f
```

## 📋 部署步骤详解

### 1. 前置要求
- ✅ 已安装 Docker Desktop（Windows/Mac）或 Docker（Linux）
- ✅ 端口 80、8080、3306、6379 未被占用
- ✅ 至少 2GB 可用磁盘空间

### 2. 一键部署命令
```bash
cd C:\Users\thor.wen\xuanxue-app\docker
docker-compose up -d --build
```

### 3. 验证部署
访问以下地址确认服务正常：
- 前端：http://localhost
- 后端API：http://localhost:8080
- API文档：http://localhost:8080/swagger-ui.html

## 🔧 常用命令

| 命令 | 说明 |
|------|------|
| `docker-compose up -d` | 启动所有服务（后台运行） |
| `docker-compose up -d --build` | 重新构建并启动 |
| `docker-compose down` | 停止并删除容器 |
| `docker-compose ps` | 查看服务状态 |
| `docker-compose logs -f` | 查看实时日志 |
| `docker-compose restart backend` | 重启后端服务 |

## 📖 详细文档

完整部署指南请查看：[部署指南.md](./部署指南.md)

## ⚠️ 注意事项

1. **首次启动**：MySQL 数据库初始化需要 1-2 分钟，请耐心等待
2. **端口冲突**：如果端口被占用，请修改 `docker-compose.yml` 中的端口映射
3. **数据持久化**：数据存储在 Docker 卷中，删除容器不会丢失数据
4. **生产环境**：部署到生产环境前，请修改默认密码和密钥

## 🆘 遇到问题？

1. 查看日志：`docker-compose logs -f`
2. 检查状态：`docker-compose ps`
3. 重启服务：`docker-compose restart`
4. 查看详细文档：[部署指南.md](./部署指南.md)
