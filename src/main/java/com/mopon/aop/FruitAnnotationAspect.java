package com.mopon.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
/**@annotation的使用方式与@within的相似，表示匹配使用@annotation指定注解标注的方法将会被环绕
 * @author zgh
 *
 */
@Aspect
@Component
public class FruitAnnotationAspect {

	@Around("@annotation(com.mopon.aop.FruitAnnotation)")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		
		System.out.println("@annotation...before");
		
		Object  o = pjp.proceed();
		
		System.out.println("@annotation...before");

		 return o;
	}
}
