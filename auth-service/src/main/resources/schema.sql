-- 鉴权服务：仅用户表（H2 开发用；生产用 MySQL 时由 docker/init.sql 初始化）
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) UNIQUE,
    email VARCHAR(100),
    avatar VARCHAR(255),
    vip_expire_time TIMESTAMP,
    status TINYINT DEFAULT 1,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
