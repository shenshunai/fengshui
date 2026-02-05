-- H2 开发环境建表脚本（与 JPA 实体对应）

-- 用户表
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

-- 用户资料表
CREATE TABLE IF NOT EXISTS user_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    birth_date DATE,
    birth_time TIME,
    birth_place VARCHAR(100),
    is_lunar TINYINT DEFAULT 0,
    gender TINYINT,
    zodiac_sign VARCHAR(20),
    chinese_zodiac VARCHAR(10),
    created_at TIMESTAMP
);

-- 八字结果表
CREATE TABLE IF NOT EXISTS bazi_results (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    birth_datetime TIMESTAMP NOT NULL,
    year_gan VARCHAR(2) NOT NULL,
    year_zhi VARCHAR(2) NOT NULL,
    month_gan VARCHAR(2) NOT NULL,
    month_zhi VARCHAR(2) NOT NULL,
    day_gan VARCHAR(2) NOT NULL,
    day_zhi VARCHAR(2) NOT NULL,
    hour_gan VARCHAR(2) NOT NULL,
    hour_zhi VARCHAR(2) NOT NULL,
    metal_count INT DEFAULT 0,
    wood_count INT DEFAULT 0,
    water_count INT DEFAULT 0,
    fire_count INT DEFAULT 0,
    earth_count INT DEFAULT 0,
    day_master VARCHAR(10),
    day_master_strength VARCHAR(10),
    favorable_elements VARCHAR(50),
    unfavorable_elements VARCHAR(50),
    ai_analysis CLOB,
    created_at TIMESTAMP
);
