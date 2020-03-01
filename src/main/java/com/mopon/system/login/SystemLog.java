package com.mopon.system.login;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解类
 * @author zgh
 *
 */
@Target({ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {

	/**
	 * 日志名称
	 * @return
	 */
	String description() default "";
	
	
	/**
	 * 日志类型
	 * 
	 */
	
	LoginTypeEnum  type() default LoginTypeEnum.OPERATION;
}
