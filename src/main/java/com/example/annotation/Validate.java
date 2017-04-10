package com.example.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
/**
 * @Description:
 * @Author tengguodong
 * @Date 2017/2/17 19:51
 */
@Target({TYPE,METHOD,CONSTRUCTOR})
@Retention(RUNTIME)
public @interface Validate {
}
