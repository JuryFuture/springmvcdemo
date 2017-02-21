package com.example.utils.test;

/**
 * @Description:
 * @Author tengguodong
 * @Date 2017/2/20 23:53
 */
public class SubClassB {
    private String c;

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "SubClassB{" +
                "c='" + c + '\'' +
                '}';
    }
}
