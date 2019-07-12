package com.xiang.cloud.consumer.service;

import com.xiang.cloud.consumer.cache.CacheExpire;
import com.xiang.cloud.consumer.entity.Book;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author lpxiangvip@126.com
 * @date 2019/5/27
 */
@Service
@CacheConfig(cacheNames = "book")
@CacheExpire(value = 30)
public class BookService {

    @Cacheable(value = "book", key = "#isbn", condition = "#isbn.length() > 3")
    public Book getOne(String isbn) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new Book(isbn, "人类简史");
    }

    @CachePut(key = "#isbn")
    public Book update(String isbn) {
        return new Book(isbn, "人类简史2");
    }

    @CacheEvict
    public void delete(String isbn) {

    }
}
