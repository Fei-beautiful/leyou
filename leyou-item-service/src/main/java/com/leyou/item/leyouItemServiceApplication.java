package com.leyou.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;


// 后台代码
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.leyou.item.mapper")   //用来扫描Mapper
public class leyouItemServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(leyouItemServiceApplication.class, args);
    }
}
