package com.example.agumentresolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.example.utils.GsonUtils;
import com.example.utils.StringUtil;
import com.example.validation.annotation.JsonArr;
import com.example.validation.annotation.JsonObj;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.example.validation.annotation.FormIncludeJson;
import org.springframework.web.multipart.MultipartRequest;

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
			Map<String, String[]> map = request.getParameterMap();
			for (Class aClass : clazzs) {
				Field[] fields = aClass.getDeclaredFields();
				for (Field field : fields) {
					String paramName = null;
					if (field.isAnnotationPresent(JsonObj.class)) {
						JsonObj jsonObj = field.getAnnotation(JsonObj.class);
						paramName = jsonObj.paramName();
						if (StringUtil.isBlank(paramName)) {
							paramName = field.getName();
						}
					} else if (field.isAnnotationPresent(JsonArr.class)) {
						JsonArr jsonArr = field.getAnnotation(JsonArr.class);
						paramName = jsonArr.paramName();
						if (StringUtil.isBlank(paramName)) {
							paramName = field.getName();
						}
					}
					if (paramName != null) {
						map.remove(paramName);
					}
				}
			}

			String name = ModelFactory.getNameForParameter(parameter);
			Object attribute = (mavContainer.containsAttribute(name) ? mavContainer.getModel().get(name)
					: createAttribute(name, parameter, binderFactory, webRequest));

			if (!mavContainer.isBindingDisabled(name)) {
				ModelAttribute ann = parameter.getParameterAnnotation(ModelAttribute.class);
				if (ann != null && !ann.binding()) {
					mavContainer.setBindingDisabled(name);
				}
			}

			WebDataBinder binder = binderFactory.createBinder(webRequest, attribute, name);
			if (binder.getTarget() != null) {
				if (!mavContainer.isBindingDisabled(name)) {
					bindRequestParameters(binder, map);
				}
				validateIfApplicable(binder, parameter);
				if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
					throw new BindException(binder.getBindingResult());
				}
			}

			// Add resolved attribute and BindingResult at the end of the model
			Map<String, Object> bindingResultModel = binder.getBindingResult().getModel();
			mavContainer.removeAttributes(bindingResultModel);
			mavContainer.addAllAttributes(bindingResultModel);

			Object object = binder.convertIfNecessary(binder.getTarget(), parameter.getParameterType(), parameter);

			for (Class aClass : clazzs) {
				Field[] fields = aClass.getDeclaredFields();
				for (Field field : fields) {
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

			return object;
		} else {
			// 参数异常
			return null;
		}
	}

	/**
	 * Extension point to create the model attribute if not found in the model.
	 * The default implementation uses the default constructor.
	 * @param attributeName the name of the attribute (never {@code null})
	 * @param methodParam the method parameter
	 * @param binderFactory for creating WebDataBinder instance
	 * @param request the current request
	 * @return the created model attribute (never {@code null})
	 */
	protected Object createAttribute(String attributeName, MethodParameter methodParam,
			WebDataBinderFactory binderFactory, NativeWebRequest request) throws Exception {

		return BeanUtils.instantiateClass(methodParam.getParameterType());
	}

	/**
	 * Extension point to bind the request to the target object.
	 * @param binder the data binder instance to use for the binding
	 * @param map the current request 的 parameterMap
	 */
	protected void bindRequestParameters(WebDataBinder binder, Map<String, String[]> map) {
		PropertyValues propertyValues = new MutablePropertyValues(map);
		binder.bind(propertyValues);
	}

	/**
	 * Check if the request is a multipart request (by checking its Content-Type header).
	 * @param request request with parameters to bind
	 */
	private boolean isMultipartRequest(WebRequest request) {
		String contentType = request.getHeader("Content-Type");
		return (contentType != null && StringUtils.startsWithIgnoreCase(contentType, "multipart"));
	}

	/**
	 * Validate the model attribute if applicable.
	 * <p>The default implementation checks for {@code @javax.validation.Valid},
	 * Spring's {@link org.springframework.validation.annotation.Validated},
	 * and custom annotations whose name starts with "Valid".
	 * @param binder the DataBinder to be used
	 * @param methodParam the method parameter
	 */
	protected void validateIfApplicable(WebDataBinder binder, MethodParameter methodParam) {
		Annotation[] annotations = methodParam.getParameterAnnotations();
		for (Annotation ann : annotations) {
			Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
			if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
				Object hints = (validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann));
				Object[] validationHints = (hints instanceof Object[] ? (Object[]) hints : new Object[] { hints });
				binder.validate(validationHints);
				break;
			}
		}
	}

	/**
	 * Whether to raise a fatal bind exception on validation errors.
	 * @param binder the data binder used to perform data binding
	 * @param methodParam the method argument
	 * @return {@code true} if the next method argument is not of type {@link Errors}
	 */
	protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter methodParam) {
		int i = methodParam.getParameterIndex();
		Class<?>[] paramTypes = methodParam.getMethod().getParameterTypes();
		boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
		return !hasBindingResult;
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
