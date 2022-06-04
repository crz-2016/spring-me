package com.app.demo;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * 描述：<br>
 * 版权：Copyright (c) 2011-2019<br>
 * 公司：北京活力天汇<br>
 * 作者：曹孝欢<br>
 * 版本：1.0<br>
 * 创建日期：2022/3/6<br>
 */
@SpringBootApplication(scanBasePackages="com.app")
@EnableFeignClients(basePackages = "com.app")
@EnableDiscoveryClient
@MapperScan("com.app.mapper")
//@EnableWebSecurity
@EnableAdminServer
@EnableScheduling
public class DemoApp {
    public static void main(String[] args) {
        SpringApplication.run(DemoApp.class, args);
    }
}
