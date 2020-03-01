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
 * 1.execution表达式 ， 可以定位到包，子包，下的所有类，所有方法，和指定类，指定方法。
 *
 */
@Aspect
@Component
public class ExecutionAspect {

	//定义切点， 表示哪些类中的方法会执行切面定义的方法
	// 通过 @pointcut 注解声明切点表达式
	@Pointcut("execution(public * com.mopon.controller.*.*(..))")
	public void aspect() {
		System.out.println("ttt.");
	}
	
	
	@Before("aspect()")
	public void doBefore() {
		System.out.println("before....");
	}
	
	@After("aspect()")
	public void doAfter() {
		System.out.println("after********");

	}
	
	
	@Around("aspect()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("doAround********");
		Object o = pjp.proceed();
		System.out.println("endAround********");
		return o;
	}
	
	/**
	 * 在连接点执行之后执行的通知（返回通知）
	 */
	@AfterReturning("aspect()")
	public void doAfterReturning() {
		System.out.println("doAfterReturning-----*");

	}
}
