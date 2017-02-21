package com.example.utils.test;

import java.util.List;

/**
 * @Description:
 * @Author tengguodong
 * @Date 2017/2/20 20:52
 */
public class ClassB extends SuperClass {
    private Integer b;

    private List<SubClassB> subClassList;


    public Integer getB() {
        return b;
    }

    public void setB(Integer b) {
        this.b = b;
    }

    public List<SubClassB> getSubClassList() {
        return subClassList;
    }

    public void setSubClassList(List<SubClassB> subClassList) {
        this.subClassList = subClassList;
    }

    @Override
    public String toString() {
        return "ClassB{" +
                "b=" + b +
                ", subClassList=" + subClassList +
                "} " + super.toString();
    }
}
