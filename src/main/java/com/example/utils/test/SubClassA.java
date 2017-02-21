package com.example.utils.test;

/**
 * @Description:
 * @Author tengguodong
 * @Date 2017/2/20 23:52
 */
public class SubClassA {
    private String c;

    public SubClassA(String c) {
        this.c = c;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "SubClassA{" +
                "c='" + c + '\'' +
                '}';
    }
}
