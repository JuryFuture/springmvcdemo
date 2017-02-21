package com.example.utils;

import com.example.utils.test.ClassA;
import com.example.utils.test.ClassB;
import com.example.utils.test.SubClassA;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author tengguodong
 * @Date 2017/2/20 20:18
 */
public class BeanUtil {
    private static final Log logger = LogFactory.getLog(BeanUtil.class);

    public static void copyProperties(Object source, Object target) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
        copyProperties(source, target, null);
    }

    /**
     * 拷贝属性，要求属性名相同，集合属性必须以list结尾
     * @param source
     * @param target
     * @param ignoreProperties
     */
    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        if (source == null) {
            logger.error(String.format("source不能为空"));
        }
        if (target == null) {
            logger.error(String.format("target不能为空"));
        }
        List<Class> superClazzs = new ArrayList<>();
        Class targetClass = target.getClass();
        while (targetClass != Object.class) {
            superClazzs.add(targetClass);
            targetClass = targetClass.getSuperclass();
        }
        try {
            if (!CollectionUtils.isEmpty(superClazzs)) {
                for (int i = 0; i < superClazzs.size(); i++) {
                    Class clazz = superClazzs.get(i);
                    Method[] methods = clazz.getDeclaredMethods();
                    for (int j = 0; j < methods.length; j++) {
                        String methodName = methods[j].getName();
                        if (Pattern.matches("^set.*(?<!List)$", methodName)) {
                            String getter = methodName.replace("set", "get");
                            Method method = null;
                            try {
                                method = source.getClass().getDeclaredMethod(getter);
                            } catch (NoSuchMethodException e) {
                                logger.error(String.format("%s类中没有方法：%s",source.getClass(),getter));
                                continue;
                            }
                            methods[j].invoke(target, method.invoke(source));
                        } else if (Pattern.matches("^get.*List$", methodName)) {
                            Type types = methods[j].getGenericReturnType();
                            int beginIndex = types.toString().indexOf("<") + 1;
                            int endIndex = types.toString().indexOf(">");
                            String className = types.toString().substring(beginIndex, endIndex);
                            Class listClass = Class.forName(className);

                            Method sourceListGetter = null;
                            try {
                                sourceListGetter = source.getClass().getDeclaredMethod(methodName);
                            } catch (NoSuchMethodException e) {
                                logger.error(String.format("%s类中没有方法：%s",source.getClass(),methods[j].getName()));
                                continue;
                            }
                            Object sourceObj = sourceListGetter.invoke(source);
                            List sourceList = (List) sourceObj;

                            List targetList = new ArrayList();

                            if (!CollectionUtils.isEmpty(sourceList)) {
                                for (int k = 0; k < sourceList.size(); k++) {
                                    Object element = listClass.newInstance();
                                    copyProperties(sourceList.get(k), element);
                                    targetList.add(element);
                                }
                            }
                            String setter = methodName.replace("get", "set");
                            Method method = null;
                            try {
                                method = target.getClass().getDeclaredMethod(setter, List.class);
                            } catch (NoSuchMethodException e) {
                                logger.error(String.format("%s类中没有方法：%s",target.getClass(),setter));
                                continue;
                            }
                            method.invoke(target, targetList);
                        } else {
                            logger.error(String.format("未执行%s.%s()方法",target.getClass(),methodName));
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(String.format("参数转换异常，%s", e));
        }
    }

    public static void main(String[] args) {
        ClassA classA = new ClassA("a", 1);
        classA.setSubClassList(Arrays.asList(new SubClassA("c")));

        ClassB classB = new ClassB();
        try {
            copyProperties(classA, classB);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        System.out.println(classB);
    }
}
