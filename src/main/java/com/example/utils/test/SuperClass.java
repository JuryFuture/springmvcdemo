package com.example.utils.test;

/**
 * @Description:
 * @Author tengguodong
 * @Date 2017/2/20 23:51
 */
public class SuperClass {
    private String a;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    @Override
    public String toString() {
        return "SuperClass{" +
                "a='" + a + '\'' +
                '}';
    }
}
