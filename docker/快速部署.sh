#!/bin/bash

echo "========================================"
echo "  玄学平台 - Docker 一键部署脚本"
echo "========================================"
echo ""

# 检查Docker是否安装
if ! command -v docker &> /dev/null; then
    echo "[错误] 未检测到Docker，请先安装Docker"
    echo "安装命令: curl -fsSL https://get.docker.com | bash"
    exit 1
fi

# 检查Docker Compose
if ! command -v docker-compose &> /dev/null; then
    echo "[错误] 未检测到Docker Compose"
    exit 1
fi

echo "[1/4] 检查Docker状态..."
if ! docker info &> /dev/null; then
    echo "[错误] Docker未运行，请启动Docker服务"
    echo "启动命令: sudo systemctl start docker"
    exit 1
fi
echo "[✓] Docker运行正常"

echo ""
echo "[2/4] 检查端口占用..."
if lsof -Pi :80 -sTCP:LISTEN -t >/dev/null 2>&1; then
    echo "[警告] 端口80已被占用，可能影响前端服务"
fi
if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null 2>&1; then
    echo "[警告] 端口8080已被占用，可能影响后端服务"
fi

echo ""
echo "[3/4] 停止旧容器（如果存在）..."
docker-compose down 2>/dev/null

echo ""
echo "[4/4] 开始构建并启动服务..."
echo "这可能需要几分钟时间，请耐心等待..."
echo ""

docker-compose up -d --build

if [ $? -ne 0 ]; then
    echo ""
    echo "[错误] 部署失败，请查看上方错误信息"
    exit 1
fi

echo ""
echo "========================================"
echo "  部署完成！"
echo "========================================"
echo ""
echo "服务访问地址："
echo "  前端应用: http://localhost"
echo "  后端API:  http://localhost:8080"
echo "  API文档:  http://localhost:8080/swagger-ui.html"
echo ""
echo "查看服务状态: docker-compose ps"
echo "查看日志:     docker-compose logs -f"
echo "停止服务:     docker-compose down"
echo ""
