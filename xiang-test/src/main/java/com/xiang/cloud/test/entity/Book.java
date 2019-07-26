package com.xiang.cloud.test.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 李鹏翔(lipengxiang1)
 * @date 2019-07-19
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {

    private String name;

    private String isbn;

    private String description;

    private String author;

    private Author authorEntity;
}
