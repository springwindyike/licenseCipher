package com.ejiahe.jeservice.util.concurrent;

import com.google.common.cache.*;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * CallBack 延迟加载器; <br/>
 * 可传入参数;
 * Created by mjf on 2016/12/17.
 */
public abstract class ScheduleDelayCallbackCaller<T> implements RemovalListener<T,Boolean>{

    private static long DEFAULT_REFRESH_TIME = 30 * 1000;

    private static int DEFAULT_EXECUTOR_THREAD_SIZE = 3;

    private long delayTime;

    private long refreshTime;

    /**
     * 利用Cache的expire进行session的周期检测;
     * <br/>
     * Key: sessionId, Value: idleCount
     */
    private Cache<T,Boolean> callbacks;

    private ScheduledExecutorService refresher;

    private Executor executor;


    public ScheduleDelayCallbackCaller(long delayTime){
        this(delayTime,DEFAULT_REFRESH_TIME);
    }

    public ScheduleDelayCallbackCaller(long delayTime, long refreshTime){
        this(delayTime,refreshTime,DEFAULT_EXECUTOR_THREAD_SIZE);
    }

    public ScheduleDelayCallbackCaller(long delayTime, long refreshTime, int executorThreadSize){
        this(delayTime,refreshTime, Executors.newFixedThreadPool(executorThreadSize));
    }

    public ScheduleDelayCallbackCaller(long delayTime, long refreshTime, Executor executor){
        this.delayTime = delayTime;
        this.refreshTime = refreshTime;
        this.executor = executor;
        init();
    }

    public void watch(T target){
        if(target != null){
            callbacks.put(target, true);
        }
    }

    public void cancelWatch(T target){
        if(target != null){
            callbacks.invalidate(target);
        }
    }

    public long getDelayTime() {
        return delayTime;
    }

    private void init() {
        callbacks = CacheBuilder.newBuilder().expireAfterWrite(this.delayTime, TimeUnit.MILLISECONDS)
                .maximumSize(Long.MAX_VALUE)
                .removalListener(this).build();

        refresher = Executors.newSingleThreadScheduledExecutor();
        refresher.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                callbacks.cleanUp();
//                System.out.println(callbacks.size());
            }
        }, 1000, refreshTime, TimeUnit.MILLISECONDS);
    }

    public void stop() throws Exception {
        callbacks.invalidateAll();
        refresher.shutdown();
        executor = null;
        callbacks = null;
    }

    @Override
    public void onRemoval(final RemovalNotification<T, Boolean> notification) {
        if(notification.getCause() == RemovalCause.EXPIRED){
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    callback(notification.getKey());
                }
            });
        }
    }

    public abstract void callback(T object);
}
