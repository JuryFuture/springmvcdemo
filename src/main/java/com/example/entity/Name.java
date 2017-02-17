package com.example.entity;

import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author tengguodong
 * @Date 2017/2/17 14:29
 */
public class Name {
    @NotNull(message = "名字不允许为空")
    private String firstName;

    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return firstName + lastName;
    }

    @Override
    public String toString() {
        return "Name{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
