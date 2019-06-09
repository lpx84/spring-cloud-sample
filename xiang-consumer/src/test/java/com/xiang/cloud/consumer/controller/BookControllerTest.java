package com.xiang.cloud.consumer.controller;

import com.xiang.cloud.consumer.XiangConsumerApplicationTests;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author lpxiangvip@126.com
 * @date 2019/6/1
 */
public class BookControllerTest extends XiangConsumerApplicationTests {

    private static final String urlPrefix = "/api/book";

    @Autowired
    private WebApplicationContext wac;

    private static MockMvc mockMvc = null;

    @Before
    public void setUp() {
        if (mockMvc == null) mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("{prefix}/{isbn}", urlPrefix, "1")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value("1"));
    }
}
