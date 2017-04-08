package com.example.agumentresolver;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.example.utils.GsonUtils;
import com.example.utils.StringUtil;
import com.example.validation.annotation.JsonArr;
import com.example.validation.annotation.JsonObj;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.validation.annotation.FormIncludeJson;

/**
 * Description:
 *
 * @author: tengguodong
 * @date: 2017/4/6
 */
public class CustomArgumentResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (parameter.hasParameterAnnotation(FormIncludeJson.class)) {
			return true;
		}
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Class<?> parameterType = parameter.getParameterType();

		List<Class> clazzs = new ArrayList<>();
		Class clazz = parameterType;
		while (clazz != Object.class) {
			clazzs.add(clazz);
			clazz = clazz.getSuperclass();
		}
		if (CollectionUtils.isNotEmpty(clazzs)) {
			HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
			for (Class aClass : clazzs) {
				Field[] fields = aClass.getDeclaredFields();
				for (Field field : fields) {

					Object object = BeanUtils.instantiate(parameter.getParameterType());
					field.setAccessible(true);

					// JSON对象
					JsonObj jsonObj = field.getAnnotation(JsonObj.class);
					if (jsonObj != null) {
						String parameterName = jsonObj.paramName();
						if (StringUtil.isBlank(parameterName)) {
							parameterName = field.getName();
						}
						String value = request.getParameter(parameterName);
						String typeName = jsonObj.typeName();
						if (StringUtil.isBlank(typeName)) {
							typeName = field.getName();
						}
						Class elementType = Class.forName(typeName);
						field.set(object, GsonUtils.getInstace().fromJson(value, elementType));
					}

					// JSON数组
					JsonArr jsonArr = field.getAnnotation(JsonArr.class);
					if (jsonArr != null) {
						String parameterName = jsonArr.paramName();
						if (StringUtil.isBlank(parameterName)) {
							parameterName = field.getName();
						}
						String value = request.getParameter(parameterName);
						String typeName = jsonArr.typeName();
						if (StringUtil.isBlank(typeName)) {
							typeName = field.getName();
						}
						Class elementType = Class.forName(typeName);
						field.set(object, resolveParameter(value, parameterName, true, elementType));
					}
				}
			}
		}
		return null;
	}

	/**
	 * 解析Json数组
	 * @param param
	 * @param key
	 * @param notNull
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> resolveParameter(String param, String key, boolean notNull, Class<T> clazz) {
		List<T> list = new ArrayList<>();

		if (StringUtil.isNotEmpty(param)) {
			try {
				Type type = new TypeToken<ArrayList<JsonObject>>() {
				}.getType();
				ArrayList<JsonObject> jsonObjects = GsonUtils.getInstace().fromJson(param, type);

				for (JsonObject jsonObject : jsonObjects) {
					list.add(GsonUtils.getInstace().fromJson(jsonObject, clazz));
				}
			} catch (Exception e) {
				throw new InvalidParameterException("参数异常:" + key);
			}
		} else if (notNull) {

		}
		return list;
	}
}
