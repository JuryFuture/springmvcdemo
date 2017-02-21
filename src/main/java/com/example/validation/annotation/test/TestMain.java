package com.example.validation.annotation.test;

import com.example.validation.annotation.NotNull;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author tengguodong
 * @Date 2017/2/21 11:34
 */
public class TestMain {
    public static void main(String[] args) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        TestAnnotationBean bean = new TestAnnotationBean();
        //bean.setName("123");

        List<Class> clazzs = new ArrayList<>();
        Class clazz = bean.getClass();
        while (clazz != Object.class) {
            clazzs.add(clazz);
            clazz = clazz.getSuperclass();
        }

        if (!CollectionUtils.isEmpty(clazzs)) {
            for (int i = 0; i < clazzs.size(); i++) {
                Method[] methods = clazzs.get(i).getDeclaredMethods();
                for (int j = 0; j < methods.length; j++) {
                    Method method = methods[j];
                    String methodName = method.getName();
                    if (Pattern.matches("^get.*$",methodName)) {
                        String fileName = methodName.replaceFirst("get","");
                        String charAtFirst = fileName.substring(0,1);
                        fileName = fileName.replaceFirst(charAtFirst,charAtFirst.toLowerCase());
                        Field field = clazzs.get(i).getDeclaredField(fileName);
                        if (field.isAnnotationPresent(NotNull.class)) {
                            NotNull notNull = field.getAnnotation(NotNull.class);
                            Object obj = method.invoke(bean);
                            if (obj == null) {
                                System.out.println(notNull.value() + "不能为空");
                            }
                        }
                    }
                }
            }
        }
    }
}
