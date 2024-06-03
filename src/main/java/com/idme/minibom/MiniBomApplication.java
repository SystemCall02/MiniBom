package com.idme.minibom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@ComponentScan(basePackages = {"com.huawei.innovation", "com.idme.minibom"})
public class MiniBomApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniBomApplication.class, args);
    }

}
