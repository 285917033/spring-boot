package com.mopon.system.login;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;

import com.google.common.base.Objects;
import com.mopon.system.login.entity.Log;
import com.mopon.system.login.entity.User;
import com.mopon.system.login.service.LogService;
import com.mopon.system.login.service.UserService;
import com.mopon.system.login.util.IpInfoUtil;
import com.mopon.system.login.util.ObjectUtil;
import com.mopon.system.login.util.ThreadPoolUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 切面类 spring aop 实现日志管理
 * 
 * @author zgh
 *
 */
@Aspect
@Component
@Slf4j
public class SystemLogAspect {

	private static final ThreadLocal<Date> beginTimeThreadLocal = new NamedThreadLocal<>("ThreadLocal beginTime");

	@Autowired
	private LogService logService;

	@Autowired
	private UserService userService;

	@Autowired
	private IpInfoUtil ipInfoUtil;
	/**
	 * @Autowired(required=true)：当使用@Autowired注解的时候，其实默认就是@Autowired(required=true)，表示注入的时候，该bean必须存在，否则就会注入失败。
	 * @Autowired(required=false)：表示忽略当前要注入的bean，如果有直接注入，没有跳过，不会报错。
	 * 
	 */
	@Autowired(required = false)
	private HttpServletRequest request;
	
	// 
	/**
	 * 1.直接采用 execution方式
	 * @Pointcut("execution(*.com.mopon.system.login.*Controller。*(..))")
	 * 
	 */
	// 2.采用注解方式  @annotation
	@Pointcut("@annotation(com.mopon.system.login.SystemLog)")
	public void controllerAspect() {}
	
	
	/**
	 * 前置通知(在方法执行之前返回)
	 * 用于拦截Controller层记录用户的操作的开始时间
	 * 
	 * 
	 * @param joinPoint
	 */
	@Before("controllerAspect()")
	public void deBefore(JoinPoint joinPoint) {
		// 绑定线程变量(该数据只有当前请求的线程可见)
		Date  beginTime = new Date();
		beginTimeThreadLocal.set(beginTime);
	}
	
	/**
	 * 后置通知()
	 * @param joinPoint
	 * @throws ClassNotFoundException 
	 */
	@AfterReturning("controllerAspect()")
	public void after(JoinPoint joinPoint) throws ClassNotFoundException {
		
		String userName = "";
		String description = getControllerMethodInfo(joinPoint).get("description").toString();
		
		/**
		 * 为有时像checkbox这样的组件会有一个name对应对个value的时候，所以该Map中键值对是<String-->String[]>的实现。
		 */
		Map<String,String[]> requestParams = request.getParameterMap();
		Log log = new Log();
		//表示后台已经登录
		if(Objects.equal(getControllerMethodInfo(joinPoint).get("type"),0)) {
			
			User user = userService.getLoginUser(request);
			if(user !=null ) {
				userName = user.getUsername();
			}
			 log.setUsername(userName);
			 
		}
            //日志标题
			log.setName(description);
			//日志类型
			log.setLogType((int)getControllerMethodInfo(joinPoint).get("type"));
			log.setRequestUrl(request.getRequestURI());
			log.setRequestType(request.getMethod());
			//请求参数
			log.setRequestParam(ObjectUtil.mapToString(requestParams));
			log.setIp(ipInfoUtil.getIpAddr(request));
            log.setCreateBy("system");
            log.setUpdateBy("system");
            log.setCreateTime(new Date());
            log.setUpdateTime(new Date());
            log.setDelFlag(0);

            
            //请求开始时间
            long beginTime = beginTimeThreadLocal.get().getTime();
		    long endTime = System.currentTimeMillis();
		    
		    //请求耗时
		    Long logElapsedTime = endTime - beginTime;
		    log.setCostTime(logElapsedTime.intValue());
		    
		    //logService.insert(log);
		    ThreadPoolUtil.getPool().execute(new SaveSystemLogThread(log,logService));
		}
	
	/**
	 * 获取注解中对方法的描述信息，用于Controller层注解
	 * @param joinPoint
	 * @return
	 * @throws ClassNotFoundException 
	 */
	private static Map<String,Object> getControllerMethodInfo(JoinPoint joinPoint) throws ClassNotFoundException{
		Map<String,Object> map = new HashMap<String,Object>(16);
		//获取目标类名
		String targetName = joinPoint.getTarget().getClass().getName();
		
		//获取方法名
		String methodName = joinPoint.getSignature().getName();
		
		System.out.println("methodName 1 = " + methodName);
		
		//获取相关参数
		Object[] arguments = joinPoint.getArgs();
		
		//生成类对象
		Class targetClass = Class.forName(targetName);
		
		Method[] methods = targetClass.getMethods();
		
		String description = "";
		
		Integer type = null;
		
		for (Method method : methods) {
			System.out.println("methodName n = " + method.getName());
			if(!method.getName().equals(methodName)) {
				continue;
			}
			
			Class<?>[] clz = method.getParameterTypes();
			if(clz.length != arguments.length) {
				//比较方法中参数个数与切点中获取的参数是否相同，
				//原因是方法可以重载
				continue;
			}

			description =	method.getAnnotation(SystemLog.class).description();
		    type = method.getAnnotation(SystemLog.class).type().ordinal();
		    map.put("description", description);
		    map.put("type", type);

		}
		
		return map;
	}
	
	//保存日志到数据库
	private static class SaveSystemLogThread implements Runnable{

		private Log log;
		private LogService logService;
		
		public SaveSystemLogThread(Log log, LogService logService) {
			this.log = log;
			this.logService = logService;
		}



		@Override
		public void run() {
			System.out.println("调用多线程的方法");
			logService.insert(log);
			
		}
		
	}
	
}
