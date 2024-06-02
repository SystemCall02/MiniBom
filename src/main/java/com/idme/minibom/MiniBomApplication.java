package com.idme.minibom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.huawei.innovation","com.idme.minibom"})
public class MiniBomApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniBomApplication.class, args);
	}

}
