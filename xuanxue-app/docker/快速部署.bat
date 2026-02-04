@echo off
chcp 65001 >nul
echo ========================================
echo   玄学平台 - Docker 一键部署脚本
echo ========================================
echo.

REM 检查Docker是否安装
docker --version >nul 2>&1
if errorlevel 1 (
    echo [错误] 未检测到Docker，请先安装Docker Desktop
    echo 下载地址: https://www.docker.com/products/docker-desktop/
    pause
    exit /b 1
)

echo [1/4] 检查Docker状态...
docker info >nul 2>&1
if errorlevel 1 (
    echo [错误] Docker未运行，请启动Docker Desktop
    pause
    exit /b 1
)
echo [✓] Docker运行正常

echo.
echo [2/4] 检查端口占用...
netstat -ano | findstr ":80 " >nul 2>&1
if not errorlevel 1 (
    echo [警告] 端口80已被占用，可能影响前端服务
)
netstat -ano | findstr ":8080 " >nul 2>&1
if not errorlevel 1 (
    echo [警告] 端口8080已被占用，可能影响后端服务
)

echo.
echo [3/4] 停止旧容器（如果存在）...
docker-compose down 2>nul

echo.
echo [4/4] 开始构建并启动服务...
echo 这可能需要几分钟时间，请耐心等待...
echo.

docker-compose up -d --build

if errorlevel 1 (
    echo.
    echo [错误] 部署失败，请查看上方错误信息
    pause
    exit /b 1
)

echo.
echo ========================================
echo   部署完成！
echo ========================================
echo.
echo 服务访问地址：
echo   前端应用: http://localhost
echo   后端API:  http://localhost:8080
echo   API文档:  http://localhost:8080/swagger-ui.html
echo.
echo 查看服务状态: docker-compose ps
echo 查看日志:     docker-compose logs -f
echo 停止服务:     docker-compose down
echo.
pause
