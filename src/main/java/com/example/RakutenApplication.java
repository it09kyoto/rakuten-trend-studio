package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 乐天爬虫应用启动类
 * 修改点：移除了 exclude 排除项，使系统能够自动加载 application.properties 中的数据库配置
 */
@SpringBootApplication
public class RakutenApplication {

    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(RakutenApplication.class, args);

        System.out.println("=======================================");
        System.out.println("   乐天爬虫系统已加载数据库配置并在本地启动！");
        System.out.println("   请访问: http://localhost:8080/analysis?genre=0&lang=cn");
        System.out.println("=======================================");
    }
}