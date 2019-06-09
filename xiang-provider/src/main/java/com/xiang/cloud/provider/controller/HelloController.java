package com.xiang.cloud.provider.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lpxiangvip@126.com
 * @date 2019/5/19
 */
@Slf4j
@RestController
public class HelloController {

    @Autowired
    private DiscoveryClient client;

    @RequestMapping("/say")
    private String say(@RequestParam(required = false) String word) {
        if (word == null) {
            word = "Hello World";
        }
        List<ServiceInstance> list = client.getInstances("hello-provider");
        list.forEach(x -> log.info("/say, host={}:{}", x.getHost(), x.getPort()));
        return "I received: " + word;
    }
}
