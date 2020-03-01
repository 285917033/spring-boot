package com.mopon.system.login.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 朱光和
 * 
 * 一定要注意一个概念，即存在于线程池中容器的一定是Thread对象，而不是您要求运行的任务（所以叫线程池而不叫任务池也不叫对象池，更不叫游泳池）；您要求运行的任务将被线程池分配给某一个空闲的Thread运行。

从上图中，我们可以看到构成线程池的几个重要元素：

等待队列：顾名思义，就是您调用线程池对象的submit()方法或者execute()方法，要求线程池运行的任务（这些任务必须实现Runnable接口或者Callable接口）。但是出于某些原因线程池并没有马上运行这些任务，而是送入一个队列等待执行（这些原因后文马上讲解）。

核心线程：线程池主要用于执行任务的是“核心线程”，“核心线程”的数量是您创建线程时所设置的corePoolSize参数决定的。如果不进行特别的设定，线程池中始终会保持corePoolSize数量的线程数（不包括创建阶段）。

非核心线程：一旦任务数量过多（由等待队列的特性决定），线程池将创建“非核心线程”临时帮助运行任务。您设置的大于corePoolSize参数小于maximumPoolSize参数的部分，就是线程池可以临时创建的“非核心线程”的最大数量。这种情况下如果某个线程没有运行任何任务，在等待keepAliveTime时间后，这个线程将会被销毁，直到线程池的线程数量重新达到corePoolSize。

要重点理解上一条描述中黑体字部分的内容。也就是说，并不是所谓的“非核心线程”才会被回收；而是谁的空闲时间达到keepAliveTime这个阀值，就会被回收。直到线程池中线程数量等于corePoolSize为止。

maximumPoolSize参数也是当前线程池允许创建的最大线程数量。那么如果您设置的corePoolSize参数和您设置的maximumPoolSize参数一致时，线程池在任何情况下都不会回收空闲线程。keepAliveTime和timeUnit也就失去了意义。

keepAliveTime参数和timeUnit参数也是配合使用的。keepAliveTime参数指明等待时间的量化值，timeUnit指明量化值单位。例如keepAliveTime=1，timeUnit为TimeUnit.MINUTES，代表空闲线程的回收阀值为1分钟。

 */
public class ThreadPoolUtil {

	// 1.线程缓冲队列
	private static BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(100);

	// 2.核心线程数，线程池初始化的时候，就会有这么大,会一直存活，即使没有任务，线程池也会维护线程的最少数量
	private static final int SIZE_CORE_POOL = 5;

	// 3.线程池维护线程的最大数量
	private static final int SIZE_MAX_POOL = 10;

	// 4.线程池维护线程所允许的空闲时间
	// 如果当前线程池中线程数大于corePoolSize,多余的线程，在等待keepAliveTime时间后如果还没有新的线程任务指派给它，它就会被回收

	private static final long ALIVE_TIME = 2000;

	// 5.创建线程池对象，用数组的阻塞队列来存储线程
	private static ThreadPoolExecutor pool = new ThreadPoolExecutor(SIZE_CORE_POOL, SIZE_MAX_POOL, ALIVE_TIME,
			TimeUnit.MILLISECONDS, queue, new ThreadPoolExecutor.CallerRunsPolicy());

	
	static {
		//prestartAllCoreThreads设置项，可以在线程池创建，但还没有接收到任何任务的情况下，先行创建符合corePoolSize参数值的线程数：
		pool.prestartAllCoreThreads();
	}
	
	public static ThreadPoolExecutor getPool() {
		
		return pool;
	}
	public static void main(String[] args) {
	
		System.out.println(pool.getPoolSize());
	}
}
