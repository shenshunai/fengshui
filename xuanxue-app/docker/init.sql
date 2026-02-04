-- 玄学平台数据库初始化脚本

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS xuanxue 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE xuanxue;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) UNIQUE,
    email VARCHAR(100),
    avatar VARCHAR(255),
    vip_expire_time DATETIME,
    status TINYINT DEFAULT 1 COMMENT '状态: 1-正常 0-禁用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_phone (phone),
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 用户资料表
CREATE TABLE IF NOT EXISTS user_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    birth_date DATE,
    birth_time TIME,
    birth_place VARCHAR(100),
    is_lunar TINYINT DEFAULT 0 COMMENT '是否农历: 0-公历 1-农历',
    gender TINYINT COMMENT '性别: 1-男 2-女',
    zodiac_sign VARCHAR(20) COMMENT '星座',
    chinese_zodiac VARCHAR(10) COMMENT '生肖',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 八字结果表
CREATE TABLE IF NOT EXISTS bazi_results (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    birth_datetime DATETIME NOT NULL,
    -- 四柱
    year_gan VARCHAR(2) NOT NULL,
    year_zhi VARCHAR(2) NOT NULL,
    month_gan VARCHAR(2) NOT NULL,
    month_zhi VARCHAR(2) NOT NULL,
    day_gan VARCHAR(2) NOT NULL,
    day_zhi VARCHAR(2) NOT NULL,
    hour_gan VARCHAR(2) NOT NULL,
    hour_zhi VARCHAR(2) NOT NULL,
    -- 五行统计
    metal_count INT DEFAULT 0,
    wood_count INT DEFAULT 0,
    water_count INT DEFAULT 0,
    fire_count INT DEFAULT 0,
    earth_count INT DEFAULT 0,
    -- 命理分析
    day_master VARCHAR(10),
    day_master_strength VARCHAR(10),
    favorable_elements VARCHAR(50),
    unfavorable_elements VARCHAR(50),
    ai_analysis TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_birth_datetime (birth_datetime)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(32) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    product_type VARCHAR(50) NOT NULL COMMENT '产品类型',
    product_id BIGINT,
    amount DECIMAL(10,2) NOT NULL,
    status TINYINT DEFAULT 0 COMMENT '状态: 0-待支付 1-已支付 2-已取消',
    pay_channel VARCHAR(20) COMMENT '支付渠道',
    transaction_id VARCHAR(64) COMMENT '交易流水号',
    pay_time DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_order_no (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 抽签记录表
CREATE TABLE IF NOT EXISTS divine_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    divine_type VARCHAR(20) NOT NULL COMMENT '抽签类型',
    sign_number INT NOT NULL,
    sign_level VARCHAR(10) NOT NULL,
    sign_poem TEXT,
    interpretation TEXT,
    question VARCHAR(500),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 初始化测试用户 (密码: 123456)
INSERT INTO users (username, password, phone) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6EKvS', '13800138000'),
('test', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6EKvS', '13800138001')
ON DUPLICATE KEY UPDATE username=username;

-- 完成
SELECT 'Database initialization completed!' AS status;
