package com.ejiahe.jeservice.util.common;

import com.ejiahe.jeservice.util.JEConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * JE任务引擎;
 * 提供便捷的方法用于异步执行任务(调用缺省的任务引擎执行);
 * 
 * 另外,该类同时也提供子任务引擎的生成和管理;
 * 上层可根据需要自行生成任务引擎;
 * 
 * @author MaiJingFeng
 */
public class JETaskEngine {
	
	private static JETaskEngine instance = null;
	
	private static Object lock = new Object();
	
	private Map<String,TaskEngine> taskEngines;

	private TaskEngine defaultTaskEngine;

	private int defaultTaskEngineMaxCoreSize = 50;
	private String defaultTaskEngineName = "default";

	protected static Logger LOG = LoggerFactory.getLogger(JETaskEngine.class);


	/**
	 * Returns a task engine instance (singleton).
	 *
	 * @return a task engine.
	 */
	public static JETaskEngine getInstance() {

		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new JETaskEngine();
				}
			}
		}

		return instance;
	}

	//timer定时器
	private Timer timer;
	//默认时间间隔(一天)
	private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

	/**
	 * Constructs a new task engine.
	 */
	private JETaskEngine() {
		timer = new Timer("TaskEngine-timer", true);

		taskEngines = new ConcurrentHashMap<String,TaskEngine>();
		defaultTaskEngine = new TaskEngine(defaultTaskEngineName, defaultTaskEngineMaxCoreSize);
		taskEngines.put(defaultTaskEngine.getName(), defaultTaskEngine);
		
	}

	/**
	 * 每天定点周期执行任务
	 * @param task 任务
	 * @param hour 0-24小时
	 */
	public void scheduleAtFixedRateWithTimer(TimerTask task, int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date date = calendar.getTime(); //第一次执行定时任务的时间
		//如果第一次执行定时任务的时间 小于当前的时间
		//此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
		if (date.before(new Date())) {
			date = this.addDay(date, 1);
		}
		//安排指定的任务在指定的时间开始进行重复的固定延迟执行。
		timer.schedule(task, date, PERIOD_DAY);
	}
	/**
	 * 每天定点定分周期执行任务
	 * @param task 任务
	 * @param hour 0-24小时
	 * @param minute 0-59分
	 */
	public void scheduleAtFixedRateWithTimer(TimerTask task, int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		Date date = calendar.getTime(); //第一次执行定时任务的时间
		//如果第一次执行定时任务的时间 小于当前的时间
		//此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
		if (date.before(new Date())) {
			date = this.addDay(date, 1);
		}
		//安排指定的任务在指定的时间开始进行重复的固定延迟执行。
		timer.schedule(task, date, PERIOD_DAY);
	}

	/**
	 * 周期执行指定任务
	 * @param task 任务
	 * @param firstTime 第一次执行时间
	 * @param period 间隔时长，单位毫秒
	 */
	public void scheduleAtFixedRateWithTimer(TimerTask task, Date firstTime, long period) {
		//安排指定的任务在指定的时间开始进行重复的固定延迟执行。
		timer.schedule(task, firstTime, period);
	}
	/**
	 * 周期执行指定任务
	 * @param task 任务
	 * @param delay 当定时器启动后的多久开始执行，单位毫秒
	 * @param period 间隔时长，单位毫秒
	 */
	public void scheduleWithTimer(TimerTask task, long delay, long period) {
		//安排指定的任务在指定的时间开始进行重复的固定延迟执行。
		timer.schedule(task, delay, period);
	}

	// 增加或减少天数
	public Date addDay(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}
	
	
	/**
	 * 创建任务引擎(若引擎已存在, 则直接返回)
	 * @param name 任务引擎名称
	 * @return
	 *
	 */
	public TaskEngine createTaskEngine(String name){
		return createTaskEngine(name, TaskEngine.DEFAULT_CORE_SIZE);
	}
	
	/**
	 * 创建任务引擎(若引擎已存在, 则直接返回)
	 * @param name 任务引擎名称
	 * @param coreSize 任务引擎内核线程数
	 * @return
	 *
	 */
	public TaskEngine createTaskEngine(String name, int coreSize){
		TaskEngine engine = taskEngines.get(name);
		if(engine == null){
			synchronized(this){
				if(engine == null){
					engine = new TaskEngine(name, coreSize);
					taskEngines.put(name, engine);
				}
			}
		}
		
		return engine;
	}
	
	
	public void removeTaskEngine(TaskEngine engine){
		if(engine != null){
			engine.dispose();
			taskEngines.remove(engine.getName());
		}
	}
	
	public void removeTaskEngine(String name){
		TaskEngine engine = taskEngines.get(name);
		removeTaskEngine(engine);
	}
	
	
	
	/**
	 * 提交任务,并返回任务Future控制;
	 * 
	 * @param task the task to submit.
	 * @return a Future representing pending completion of the task, and whose
	 *         <tt>get()</tt> method will return <tt>null</tt> upon completion.
	 * @throws java.util.concurrent.RejectedExecutionException if task cannot be
	 *             scheduled for execution.
	 * @throws NullPointerException if task null.
	 */
	public Future<?> submit(Runnable task) {
		return defaultTaskEngine.submit(task);
	}
	
	
	public <T> Future<T> submit(Runnable task, T result) {
		return defaultTaskEngine.submit(task, result);
	}
	
	
	public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
		// TODO Auto-generated method stub
		return defaultTaskEngine.schedule(command, delay, unit);
	}
	
	
	/**
	 * 周期并发执行指定任务,不等待上一个任务的结束
	 * 
	 * @param command 任务
	 * @param initialDelay 初次延迟加载
	 * @param period 周期时间
	 * @param unit 时间单位
	 * @return
	 */
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period,
			TimeUnit unit) {
		// TODO Auto-generated method stub
		return defaultTaskEngine.scheduleAtFixedRate(command, initialDelay, period, unit);
	}
	
	
	/**
	 * 周期执行指定任务,在特定时间后触发;
	 * 
	 * @param task 任务
	 * @param firstTime 首次触发时间(若<当前时间,则马上触发)
	 * @param period 周期
	 * @return
	 */
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date firstTime, long period) {
		
		long now = System.currentTimeMillis();
		long firstTimeMillisends = firstTime.getTime();
		long delay = 0;
		if (now < firstTimeMillisends) {
			delay = firstTimeMillisends - now;
		}
		
		return defaultTaskEngine.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
	}
	
	
	/**
	 * 循环延时执行指定任务,在上一个任务结束,并等待指定延时后,触发新一轮任务
	 * 
	 * @param command 任务
	 * @param initialDelay 延迟加载
	 * @param delay 周期延迟
	 * @param unit 时间单位
	 * @return
	 */
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay,
			long delay, TimeUnit unit) {
		// TODO Auto-generated method stub
		return defaultTaskEngine.scheduleWithFixedDelay(command, initialDelay, delay, unit);
	}
	
//	/**
//	 * 指定周期触发的时间(Cron表达式,默认时区), 并周期自动执行任务;
//	 * <p>
//	 * Cron表达式示例:
//	 * <ul>
//	 * <li>"0 0 * * * *" = the top of every hour of every day.</li>
//	 * <li>"*&#47;10 * * * * *" = every ten seconds.</li>
//	 * <li>"0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.</li>
//	 * <li>"0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.</li>
//	 * <li>"0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays</li>
//	 * <li>"0 0 0 25 12 ?" = every Christmas Day at midnight</li>
//	 * </ul>
//	 * 
//	 * @param task 要执行的任务
//	 * @param cronPattern Cron表达式
//	 * @return
//	 *
//	 */
//	public ScheduledFuture<?> scheduleWithCronTrigger(Runnable task, String cronPattern){
//		return defaultTaskEngine.scheduleWithCronTrigger(task, cronPattern);
//	}
//	
//	/**
//	 * 指定周期触发的时间(Cron表达式,指定时区), 并周期自动执行任务;
//	 * <p>
//	 * Cron表达式示例:
//	 * <ul>
//	 * <li>"0 0 * * * *" = the top of every hour of every day.</li>
//	 * <li>"*&#47;10 * * * * *" = every ten seconds.</li>
//	 * <li>"0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.</li>
//	 * <li>"0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.</li>
//	 * <li>"0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays</li>
//	 * <li>"0 0 0 25 12 ?" = every Christmas Day at midnight</li>
//	 * </ul>
//	 * @param task 要执行的任务
//	 * @param cronPattern Cron表达式
//	 * @param timeZone Cron表达式所应用的时区
//	 * @return
//	 *
//	 */
//	public ScheduledFuture<?> scheduleWithCronTrigger(Runnable task, String cronPattern, TimeZone timeZone){
//		return defaultTaskEngine.scheduleWithCronTrigger(task, cronPattern, timeZone);
//	}
//	
//	/**
//	 * 在指定时刻每天周期执行任务(一天一次), 若指定时刻<当前时间,则延迟至第二天执行首次任务
//	 * @param task 被执行的任务
//	 * @param hours 小时,0<=hours<=23
//	 * @param minutes 分钟, 0<=minutes<=59
//	 * @param seconds 秒, 0<=seconds<=59
//	 * @return
//	 *
//	 */
//	public ScheduledFuture<?> schedulePerDate(Runnable task,int hours, int minutes, int seconds){
//		return defaultTaskEngine.schedulePerDate(task, hours, minutes, seconds);
//	}
	
	/**
	 * 销毁引擎
	 * 
	 *
	 */
	public void dispose() {
		
		for(TaskEngine taskEngine : taskEngines.values()){
			taskEngine.dispose();
		}
		taskEngines = null;
		
		defaultTaskEngine = null;
		
		
		if (instance != null) {
			instance = null;
		}
		
	}
	
	
	/**
	 * 任务引擎;
	 * 
	 * 
	 * @author MaiJingFeng
	 */
	public class TaskEngine {
		
		protected static final long DEFAULT_KEEP_ALIVE = 10 * JEConstants.MINUTE;
		
		protected static final long DEFAULT_AWAIT_TERMERATION = 3 * JEConstants.SECOND;
		
		protected static final int DEFAULT_CORE_SIZE = 3;
		private String name;
		private ScheduledThreadPoolExecutor executor;
		
//		private ConcurrentTaskScheduler taskScheduler;
		
		protected TaskEngine(String engineName){
			this(engineName,DEFAULT_CORE_SIZE);
		}
		
		protected TaskEngine(String engineName, int coreSize) {
			if (coreSize <= 0) {
				executor = new ScheduledThreadPoolExecutor(DEFAULT_CORE_SIZE,
						new DefaultThreadFactory(engineName));
			} else {
				executor = new ScheduledThreadPoolExecutor(coreSize, new DefaultThreadFactory(
						engineName));
			}
			
			executor.setKeepAliveTime(DEFAULT_KEEP_ALIVE, TimeUnit.MILLISECONDS);
			executor.allowCoreThreadTimeOut(true);
			
			this.name = engineName;
			
//			taskScheduler = new ConcurrentTaskScheduler(executor,executor);
		}
		
		/**
		 * 获取任务引擎的名称
		 * @return
		 *
		 */
		public String getName() {
			return this.name;
		}
		
		/**
		 * 设置核心线程数大小
		 * @param coreSize
		 *
		 */
		public void setCoreSize(int coreSize){
			executor.setCorePoolSize(coreSize);
		}
		
		public int getCoreSize(){
			return executor.getCorePoolSize();
		}
		
		/**
		 * Submits a Runnable task for execution and returns a Future
		 * representing that task.
		 * 
		 * @param task the task to submit.
		 * @return a Future representing pending completion of the task, and
		 *         whose <tt>get()</tt> method will return <tt>null</tt> upon
		 *         completion.
		 * @throws java.util.concurrent.RejectedExecutionException if task
		 *             cannot be scheduled for execution.
		 * @throws NullPointerException if task null.
		 */
		public Future<?> submit(Runnable task) {
			return executor.submit(task);
		}
		
		
		public <T> Future<T> submit(Runnable task, T result) {
			return executor.submit(task, result);
		}
		
		
		public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
			// TODO Auto-generated method stub
			return executor.schedule(command, delay, unit);
		}
		
		
		/**
		 * 周期并发执行指定任务,不等待上一个任务的结束
		 * 
		 * @param command
		 * @param initialDelay
		 * @param period
		 * @param unit
		 * @return
		 */
		public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay,
				long period, TimeUnit unit) {
			// TODO Auto-generated method stub
			return executor.scheduleAtFixedRate(command, initialDelay, period, unit);
		}
		
		
		/**
		 * 周期执行指定任务,在特定时间后触发;
		 * 
		 * @param task 任务
		 * @param firstTime 首次触发事件
		 * @param period 周期
		 * @return
		 */
		public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date firstTime, long period) {
			
			long now = System.currentTimeMillis();
			long firstTimeMillisends = firstTime.getTime();
			long delay = 0;
			if (now < firstTimeMillisends) {
				delay = firstTimeMillisends - now;
			}
			
			return executor.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
		}
		
		
		/**
		 * 循环延时执行指定任务,在上一个任务结束,并等待指定延时后,触发新一轮任务
		 * 
		 * @param command
		 * @param initialDelay
		 * @param delay
		 * @param unit
		 * @return
		 */
		public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay,
				long delay, TimeUnit unit) {
			// TODO Auto-generated method stub
			return executor.scheduleWithFixedDelay(command, initialDelay, delay, unit);
		}
		
//		/**
//		 * 指定周期触发的时间(Cron表达式,默认时区), 并周期自动执行任务;
//		 * <p>
//		 * Cron表达式示例:
//		 * <ul>
//		 * <li>"0 0 * * * *" = the top of every hour of every day.</li>
//		 * <li>"*&#47;10 * * * * *" = every ten seconds.</li>
//		 * <li>"0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.</li>
//		 * <li>"0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.</li>
//		 * <li>"0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays</li>
//		 * <li>"0 0 0 25 12 ?" = every Christmas Day at midnight</li>
//		 * </ul>
//		 * 
//		 * @param task 要执行的任务
//		 * @param cronPattern Cron表达式
//		 * @return
//		 *
//		 */
//		public ScheduledFuture<?> scheduleWithCronTrigger(Runnable task, String cronPattern){
//			CronTrigger trigger = new CronTrigger(cronPattern);
//			return this.taskScheduler.schedule(task, trigger);
//		}
//		
//		/**
//		 * 指定周期触发的时间(Cron表达式,指定时区), 并周期自动执行任务;
//		 * <p>
//		 * Cron表达式示例:
//		 * <ul>
//		 * <li>"0 0 * * * *" = the top of every hour of every day.</li>
//		 * <li>"*&#47;10 * * * * *" = every ten seconds.</li>
//		 * <li>"0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.</li>
//		 * <li>"0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.</li>
//		 * <li>"0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays</li>
//		 * <li>"0 0 0 25 12 ?" = every Christmas Day at midnight</li>
//		 * </ul>
//		 * @param task 要执行的任务
//		 * @param cronPattern Cron表达式
//		 * @param timeZone Cron表达式所应用的时区
//		 * @return
//		 *
//		 */
//		public ScheduledFuture<?> scheduleWithCronTrigger(Runnable task, String cronPattern, TimeZone timeZone){
//			if(timeZone == null){
//				return scheduleWithCronTrigger(task, cronPattern);
//			}
//			CronTrigger trigger = new CronTrigger(cronPattern,timeZone);
//			return this.taskScheduler.schedule(task, trigger);
//		}
//		
//		/**
//		 * 在指定时刻每天周期执行任务(一天一次)
//		 * @param task 被执行的任务
//		 * @param hours 小时,0<=hours<=23
//		 * @param minutes 分钟, 0<=minutes<=59
//		 * @param seconds 秒, 0<=seconds<=59
//		 * @return
//		 *
//		 */
//		public ScheduledFuture<?> schedulePerDate(Runnable task,int hours, int minutes, int seconds){
//			if(hours > 24 || hours < 0){
//				throw new IllegalArgumentException("thr hours can not lager than 23 or less than 0");
//			}
//			
//			if(minutes > 59 || minutes < 0){
//				minutes = 0;
//			}
//			
//			if(seconds > 59 || seconds < 0){
//				seconds = 0;
//			}
//			
//			CronTrigger trigger = new CronTrigger(seconds + " " + minutes + " " + hours + " * * ?");
//			return this.taskScheduler.schedule(task, trigger);
//		}
		
		
		public void dispose() {
			if(executor != null){
				executor.shutdownNow();
				try {
					executor.awaitTermination(DEFAULT_AWAIT_TERMERATION, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					LOG.error(e.getMessage());
				}
				
				executor = null;
				
				removeTaskEngine(this);
			}
		}
		
	}
	
	/**
	 * 默认线程工厂类
	 * 
	 * @author MaiJingFeng
	 * 
	 */
	private class DefaultThreadFactory implements ThreadFactory {
		
		private String name;
		
		
		protected DefaultThreadFactory(String name) {
			this.name = name;
		}
		
		
		final AtomicInteger threadNumber = new AtomicInteger(1);
		
		
		public Thread newThread(Runnable runnable) {
			// Use our own naming scheme for the threads.
			Thread thread = new Thread(Thread.currentThread().getThreadGroup(), runnable,
					"pool-eim-" + name + "-taskEngine-" + threadNumber.getAndIncrement(), 0);
			// Make workers daemon threads.
			thread.setDaemon(true);
			if (thread.getPriority() != Thread.NORM_PRIORITY) {
				thread.setPriority(Thread.NORM_PRIORITY);
			}
			return thread;
		}
		
	}
	
}
