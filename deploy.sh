#!/bin/bash
set -e
cd "$(dirname "$0")"

echo "========================================"
echo "  SSA-Project 一键部署 (Docker)"
echo "========================================"

if [ ! -f docker/docker-compose.yml ]; then
    echo "[错误] 未找到 docker/docker-compose.yml"
    exit 1
fi

if [ ! -f docker/.env ]; then
    if [ -f docker/.env.example ]; then
        cp docker/.env.example docker/.env
        echo "[提示] 已从 .env.example 复制生成 .env，请按需修改后重新运行。"
        exit 0
    fi
fi

echo "[1/2] 构建并启动所有服务..."
cd docker
docker-compose up -d --build
cd ..

echo ""
echo "[2/2] 等待 Nacos 就绪..."
sleep 15

echo ""
echo "========================================"
echo "  部署完成"
echo "========================================"
echo "  前端:    http://localhost:80"
echo "  网关:    http://localhost:8080"
echo "  Nacos:   http://localhost:8848/nacos  (nacos/nacos)"
echo "  MySQL:   localhost:3306"
echo "  Redis:   localhost:6379"
echo "  RabbitMQ: http://localhost:15672  (admin/admin123)"
echo "  Kafka:   localhost:9092"
echo "========================================"
