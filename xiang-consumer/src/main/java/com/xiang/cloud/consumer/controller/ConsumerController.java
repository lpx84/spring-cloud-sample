package com.xiang.cloud.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * @author lpxiangvip@126.com
 * @date 2019/5/19
 */
@RestController
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/say")
    private ResponseEntity<String> say(@RequestParam(required = false) String word) {
        return restTemplate.getForEntity("http://HELLO-PROVIDER/say?word={word}", String.class, new HashMap<String, Object>() {{
            put("word", word);
        }});
    }
}
