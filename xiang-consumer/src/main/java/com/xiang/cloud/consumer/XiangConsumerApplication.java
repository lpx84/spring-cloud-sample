package com.xiang.cloud.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableCaching
@EnableDiscoveryClient
@SpringBootApplication
public class XiangConsumerApplication {

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		RestTemplate t = new RestTemplate();
		return t;
	}

	public static void main(String[] args) {
		SpringApplication.run(XiangConsumerApplication.class, args);
	}

}
