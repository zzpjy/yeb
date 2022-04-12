package com.xxxx.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * 启动类
 */
@SpringBootApplication
@MapperScan("com.xxxx.server.mapper")
//开启定时任务
@EnableScheduling
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class YebApplication {

    public static void main(String[] args){
        SpringApplication.run(YebApplication.class,args);
    }

}
