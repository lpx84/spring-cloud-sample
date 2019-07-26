package com.xiang.cloud.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@EnableCaching
@SpringBootApplication(scanBasePackages = {
		"com.xiang.cloud.test",
		"com.xiang.cloud.cache",
})
public class XiangTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(XiangTestApplication.class, args);
	}

}
