package com.example.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
/**
 * @Description:
 * @Author tengguodong
 * @Date 2017/2/17 19:53
 */
@Target({FIELD,METHOD,CONSTRUCTOR,PARAMETER})
@Retention(RUNTIME)
public @interface NotEmpty {
}
