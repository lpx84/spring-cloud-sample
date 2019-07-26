package com.xiang.cloud.test.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 李鹏翔(lipengxiang1)
 * @date 2019-07-22
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author implements Serializable {

    private String name;

    private String address;

    private String company;
}
