package com.xiang.cloud.test.service;

import com.xiang.cloud.test.entity.Author;
import org.springframework.stereotype.Service;

/**
 * @author 李鹏翔(lipengxiang1)
 * @date 2019-07-22
 **/
@Service
public class AuthorService {


    public Author getByName(String name) {
        return Author.builder().name(name).address("Beijing").company("MI").build();
    }

}
