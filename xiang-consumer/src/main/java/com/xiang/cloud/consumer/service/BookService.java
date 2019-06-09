package com.xiang.cloud.consumer.service;

import com.xiang.cloud.consumer.entity.Book;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author lpxiangvip@126.com
 * @date 2019/5/27
 */
@Service
public class BookService {


    @Cacheable({"books", "isbns"})
    public Book getOne(String isbn) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new Book(isbn, "人类简史");
    }

    @CachePut("books")
    public Book update(String isbn) {
        return new Book(isbn, "人类简史2");
    }
}
