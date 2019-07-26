package com.xiang.cloud.test.service;

import com.xiang.cloud.cache.annotantion.CacheExpire;
import com.xiang.cloud.test.entity.Book;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;

/**
 * @author 李鹏翔(lipengxiang1)
 * @date 2019-07-19
 **/
@Slf4j
@Service
@CacheExpire
@CacheConfig(cacheNames = "book")
public class BookService {


    @Autowired
    private AuthorService authorService;

    @CacheExpire(value = 30)
    @Caching(cacheable = @Cacheable(key = "#isbn"))
    public Book query(String isbn) {
        log.info("Enter method...");
        try {
            return Book.builder()
                    .authorEntity(authorService.getByName("lipengxiang123"))
                    .isbn(isbn)
                    .name("人性的弱点")
                    .description(IOUtils.toString(new FileInputStream("/Users/percy/tmp/kryo.htm")))
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public Book queryByName(String authorName) {
        return Book.builder()
                .authorEntity(authorService.getByName(authorName))
//                .author("lipengxiang123")
                .isbn("12345")
                .name("人性的弱点")
                .build();
    }


    @CachePut(key = "#isbn")
    public Book update(String isbn, String author) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Book book = query(isbn);
        book.setAuthor(author);
        return book;
    }

    @CacheEvict(key = "#isbn")
    public void delete(String isbn) {

    }
}
