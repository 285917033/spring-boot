package com.mopon.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
/**
 * @within 自定义注解，作用与整个类的所有方法
 * @author zgh
 *
 */
@Aspect
@Component
public class FruitAnnotationWithinAspect {

	@Around("@within(com.mopon.aop.FruitAnnotation)")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		
		System.out.println("@within...before");
		
		Object  o = pjp.proceed();
		
		System.out.println("@within...before");

		 return o;
	}
}
