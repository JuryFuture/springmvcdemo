package com.example.entity;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.example.annotation.JsonArr;

/**
 * @Description:
 * @Author tengguodong
 * @Date 2017/2/17 14:29
 */
public class User {
	@Valid
	@JsonArr(typeName = "com.example.entity.Name")
	private List<Name> names;
	@NotNull(message = "密码不允许为空")
	private String passWord;
	private int age;

	public List<Name> getNames() {
		return names;
	}

	public void setNames(List<Name> names) {
		this.names = names;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "User{" + "names=" + names + ", passWord='" + passWord + '\'' + ", age=" + age + '}';
	}
}
