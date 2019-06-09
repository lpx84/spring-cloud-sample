package com.xiang.cloud.consumer.controller;

import com.xiang.cloud.consumer.entity.Book;
import com.xiang.cloud.consumer.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lpxiangvip@126.com
 * @date 2019/5/27
 */
@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/{isbn}")
    public Book get(@PathVariable("isbn") String isbn) {
        return bookService.getOne(isbn);
    }

    @GetMapping("/update/{isbn}")
    public Book update(@PathVariable("isbn") String isbn) {
        return bookService.update(isbn);
    }

}
