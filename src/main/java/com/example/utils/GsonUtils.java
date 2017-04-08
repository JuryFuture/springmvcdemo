/** 
 * Project Name: mq_project 
 * File Name: GsonUtils.java 
 * Package Name: com.huifenqi.mq.util 
 * Date: 2015年11月27日下午5:40:15 
 * Copyright (c) 2015, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.example.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/** 
 * ClassName: GsonUtils
 * date: 2015年11月27日 下午5:40:15
 * Description: JSON工具类,使用的是google的JSON
 * 
 * @author xiaozhan 
 * @version  
 * @since JDK 1.8 
 */
public class GsonUtils {

	private final static Gson instance = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss")
			.create();

	public static Gson getInstace() {
		return instance;
	}

	private static Gson lowerCaseUnderscoresInstance = new GsonBuilder().serializeNulls()
			.setDateFormat("yyyy-MM-dd HH:mm:ss").setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			.create();

	public static Gson getLowerCaseUnderscoresInstance() {
		return lowerCaseUnderscoresInstance;
	}
}
