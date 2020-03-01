package com.mopon.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 定义切面类，
 * 1.@Aspect注解 使之成为切面类
 * 2.@Component 注解，把切面类加入IOC容器中
 * @author zgh
 * 
 * 1.within表达式的粒度为类，其参数为全路径的类名（可使用通配符），表示匹配当前表达式的所有类都将被当前方法环绕。
 *
 */
@Aspect
@Component
public class WithinAspect {

	//定义切点， 表示哪些类中的方法会执行切面定义的方法
	// 通过 @pointcut 注解声明切点表达式
	@Pointcut("within(com.mopon.controller.TestController)")
	public void withAspect() {
		System.out.println("with.");
	}
	
	
	
	
	
	@Around("withAspect()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("withAspect********");
		Object o = pjp.proceed();
		System.out.println("withAspect********");
		return o;
	}
	
	
}
