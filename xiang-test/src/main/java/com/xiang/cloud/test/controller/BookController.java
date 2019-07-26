package com.xiang.cloud.test.controller;

import com.xiang.cloud.test.entity.Book;
import com.xiang.cloud.test.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author 李鹏翔(lipengxiang1)
 * @date 2019-07-19
 **/
@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/get/{isbn}")
    public Book query(@PathVariable String isbn) {
        return bookService.query(isbn);
    }

    @RequestMapping("/update/{isbn}")
    public Book update(@PathVariable String isbn, String author) {
        return bookService.update(isbn, author);
    }
}
