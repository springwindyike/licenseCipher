package com.ejiahe.jeservice.util.eventbus;

import com.ejiahe.jeservice.util.Constants;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 事件总线抽象, 该类提供对任意类型事件的监听和分发(基于google common eventbus bus) ;
 * 该事件总线是线程安全的,且支持同步和异步分发事件.
 * <ul>
 * <strong>备注:</strong>
 * <li>事件分发的结果会影响后续业务,可选择post(),即同步分发事件;
 * <li>事件分发的结果不会影响后续业务,则可选择asyncPost(),即异步分发事件;
 * <li>无论是同步或异步事件分发, 注册于事件总监上的监听器,都应当保证对事件响应的速度;
 * <li>若需要对事件进行较长时间的响应处理, 则监听器应该在响应方法中开启独立的线程进行;
 *
 * @author MaiJingFeng
 */
public class EventBus {

    public static final String DEFAULT_EVENT_BUS_NAME = "default";

    public static final int DEFAULT_EVENT_BUS_POOL_SIZE = 200;

    private static final int DEFAULT_MAX_QUEUE_LENGTH = Integer.MAX_VALUE;

    private static final long DEFAULT_THREAD_POOL_KEEPLIVE = 10 * Constants.MINUTE;

    private com.google.common.eventbus.EventBus bus;

    private com.google.common.eventbus.AsyncEventBus asyncBus;

    private static Logger Log = LoggerFactory.getLogger(EventBus.class);

    private String name;

    private Set<String> listeners = new HashSet<String>();

    private ThreadPoolExecutor executor;

    /**
     * 构造默认事件总线;
     * 等同EventBus("default",DEFAULT_EVENT_BUS_POOL_SIZE)
     */
    public EventBus(){
        this(DEFAULT_EVENT_BUS_NAME,DEFAULT_EVENT_BUS_POOL_SIZE);
    }

    public EventBus(String name){
        this(name,DEFAULT_EVENT_BUS_POOL_SIZE);
    }

    /**
     *
     * @param name 事件总线名, 便于日志分析;
     * @param threadPoolSize 异步事件分发的最大并发线程数
     */
    public EventBus(String name, int threadPoolSize){
        if(name == null){
            this.name = DEFAULT_EVENT_BUS_NAME;
        }else{
            this.name = name;
        }

        LoggingSubscriberExceptionHandler exceptionHandler =
                new LoggingSubscriberExceptionHandler(this.name);
        LoggingSubscriberExceptionHandler async_exceptionHandler =
                new LoggingSubscriberExceptionHandler(this.name+ "_async");

        bus = new com.google.common.eventbus.EventBus(exceptionHandler);

        executor = new ThreadPoolExecutor(threadPoolSize, threadPoolSize, DEFAULT_THREAD_POOL_KEEPLIVE, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(DEFAULT_MAX_QUEUE_LENGTH),
                new DefaultThreadFactory(this.name));
        executor.allowCoreThreadTimeOut(true);

        asyncBus = new com.google.common.eventbus.AsyncEventBus(this.executor,async_exceptionHandler);
    }


    /**
     * 添加事件监听器
     * @param listener 标注有@Subscribe的监听器
     *
     */
    public void addListener(Object listener){
        if(listener != null){
            try{
                bus.register(listener);
                asyncBus.register(listener);

                listeners.add(listener.toString());

                Log.debug("EventBus({}) add a listener: {}", getName(), listener.toString());
            }catch(Exception e){
                Log.error(e.getMessage(),e);
            }
        }
    }

    /**
     * 移除事件监听器
     * @param listener
     *
     */
    public void removeListener(Object listener){
        if(listener != null){
            try{
                bus.unregister(listener);
                asyncBus.unregister(listener);

                listeners.remove(listener.toString());

                Log.debug("EventBus({}) remove a listener: {}", getName(), listener.toString());
            } catch (Exception e){
                Log.error(e.getMessage(),e);
            }

        }
    }

    /**
     * 在事件总线上同步发布任意事件;
     * <br/>
     * note:该方法会等待事件分发执行完毕后返回
     * @see com.google.common.eventbus.EventBus
     * @param event
     *
     */
    public void post(Object event){
        if(event != null){
            try{
                bus.post(event);
                Log.debug("EventBus({}) post an eventbus: {}", getName(), event.toString());
            }catch(Exception e){
                Log.error(e.getMessage(),e);
            }
        }
    }

    /**
     * 在事件总线上异步发布任意事件;
     * <br/>
     * note:该方法会马上返回
     * @see com.google.common.eventbus.AsyncEventBus
     * @param event
     *
     */
    public void asyncPost(Object event){
        if(event != null){
            try{
                asyncBus.post(event);
                Log.debug("EventBus({}) post an eventbus: {}", getName(), event.toString());
            }catch(Exception e){
                Log.error(e.getMessage(),e);
            }
        }
    }

    /**
     * 获取总线标识名
     * @return
     *
     */
    public String getName(){
        return this.name;
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
                    "pool-" + name + "-EventBus-" + threadNumber.getAndIncrement(), 0);
            // Make workers daemon threads.
            thread.setDaemon(true);
            if (thread.getPriority() != Thread.NORM_PRIORITY) {
                thread.setPriority(Thread.NORM_PRIORITY);
            }
            return thread;
        }

    }


    /**
     * Simple logging handler for subscriber exceptions.
     */
    private static final class LoggingSubscriberExceptionHandler
            implements SubscriberExceptionHandler {

        /**
         * Logger for eventbus dispatch failures.  Named by the fully-qualified name of
         * this class, followed by the identifier provided at construction.
         */
        private final Logger logger;

        /**
         * @param identifier a brief name for this bus, for logging purposes. Should
         *        be a valid Java identifier.
         */
        public LoggingSubscriberExceptionHandler(String identifier) {
            logger = LoggerFactory.getLogger(
                    EventBus.class.getName() + "." + checkNotNull(identifier));
        }

        @Override
        public void handleException(Throwable exception,
                                    SubscriberExceptionContext context) {
            logger.error("Could not dispatch eventbus: "
                            + context.getSubscriber() + " to " + context.getSubscriberMethod(),
                    exception);
        }
    }

}
