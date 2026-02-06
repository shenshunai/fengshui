package com.xuanxue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 玄学应用平台主启动类
 * 包含：星座运势、八字算命、风水测算、起名服务、抽签占卜
 */
@SpringBootApplication
public class XuanxueApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(XuanxueApplication.class, args);
        System.out.println("========================================");
        System.out.println("  玄学应用平台启动成功！");
        System.out.println("  API文档: http://localhost:8080/swagger-ui.html");
        System.out.println("  H2控制台: http://localhost:8080/h2-console");
        System.out.println("========================================");
    }
}
