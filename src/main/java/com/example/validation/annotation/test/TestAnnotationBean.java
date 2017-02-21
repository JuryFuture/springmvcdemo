package com.example.validation.annotation.test;

import com.example.validation.annotation.NotNull;

/**
 * @Description:
 * @Author tengguodong
 * @Date 2017/2/21 11:33
 */
public class TestAnnotationBean {
    @NotNull("姓名")
    private String name;
    @NotNull("值")
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
