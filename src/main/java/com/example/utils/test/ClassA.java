package com.example.utils.test;

import java.util.List;

/**
 * @Description:
 * @Author tengguodong
 * @Date 2017/2/20 20:51
 */
public class ClassA {
    private String a;
    private Integer b;
    private List<SubClassA> subClassList;

    public ClassA(String a, Integer b) {
        this.a = a;
        this.b = b;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public Integer getB() {
        return b;
    }

    public void setB(Integer b) {
        this.b = b;
    }

    public List<SubClassA> getSubClassList() {
        return subClassList;
    }

    public void setSubClassList(List<SubClassA> subClassList) {
        this.subClassList = subClassList;
    }

    @Override
    public String toString() {
        return "ClassA{" +
                "a='" + a + '\'' +
                ", b=" + b +
                ", subClassList=" + subClassList +
                '}';
    }
}
