package com.example.entity;

import com.example.validation.annotation.JsonObj;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author tengguodong
 * @Date 2017/2/17 14:29
 */
public class User {
    @Valid
    @JsonObj(typeName = "com.example.entity.Name")
    private Name name;
    @NotNull(message = "密码不允许为空")
    private String passWord;

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    @Override
    public String toString() {
        return "User{" +
                "name=" + name +
                ", passWord='" + passWord + '\'' +
                '}';
    }
}
