package com.xiang.cloud.test.controller;

import com.xiang.cloud.test.XiangTestApplicationTests;
import com.xiang.cloud.test.entity.Author;
import com.xiang.cloud.test.entity.Book;
import com.xiang.cloud.test.service.AuthorService;
import com.xiang.cloud.test.service.BookService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author 李鹏翔(lipengxiang1)
 * @date 2019-07-22
 **/
public class BookControllerTest extends XiangTestApplicationTests {


//    @Autowired
    @MockBean
    private AuthorService authorService;

    @Autowired
    private BookService bookService;


    @Before
    public void before() {
        Mockito.when(authorService.getByName("abc")).thenReturn(Author.builder().name("abc").company("zhihu").build());
        Mockito.when(authorService.getByName("abc123")).thenReturn(null);
    }

    @Test
    public void testMockGet() throws Exception {

        Assert.assertEquals(bookService.queryByName("abc").getAuthorEntity().getName(), "abc");
        Assert.assertNull(bookService.queryByName("abc123").getAuthorEntity());

    }

    @Test
    public void testMockMvc() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/book/get/1234")).andExpect(status().isOk()).andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

}
