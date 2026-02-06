@echo off
chcp 65001 >nul
echo ========================================
echo   SSA-Project 一键部署 (Docker)
echo ========================================
cd /d "%~dp0"

if not exist "docker\docker-compose.yml" (
    echo [错误] 未找到 docker\docker-compose.yml
    pause
    exit /b 1
)

if not exist "docker\.env" (
    if exist "docker\.env.example" (
        copy "docker\.env.example" "docker\.env"
        echo [提示] 已从 .env.example 复制生成 .env，请按需修改后重新运行。
        pause
        exit /b 0
    )
)

echo [1/2] 构建并启动所有服务...
cd docker
docker-compose up -d --build
cd ..

if %ERRORLEVEL% neq 0 (
    echo [失败] 启动异常，请检查 Docker 是否运行及日志。
    pause
    exit /b 1
)

echo.
echo [2/2] 等待 Nacos 就绪...
timeout /t 15 /nobreak >nul

echo.
echo ========================================
echo   部署完成
echo ========================================
echo   前端:    http://localhost:80
echo   网关:    http://localhost:8080
echo   Nacos:   http://localhost:8848/nacos  (nacos/nacos)
echo   MySQL:   localhost:3306
echo   Redis:   localhost:6379
echo   RabbitMQ: http://localhost:15672  (admin/admin123)
echo   Kafka:   localhost:9092
echo ========================================
pause
