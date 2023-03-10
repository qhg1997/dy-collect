package com.qhg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.qhg.dy.mapper")
@EnableTransactionManagement
public class DyApplication {
    public static void main(String[] args) {
        SpringApplication.run(DyApplication.class, args);
    }

}
