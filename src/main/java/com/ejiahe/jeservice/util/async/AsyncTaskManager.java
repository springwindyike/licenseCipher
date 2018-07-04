package com.ejiahe.jeservice.util.async;

import com.ejiahe.jeservice.util.JEConstants;
import com.ejiahe.jeservice.util.concurrent.CacheFactory;
import com.google.common.cache.Cache;

import javax.inject.Named;
import java.util.Map;

@Named
public class AsyncTaskManager {


    Cache<String, AsyncTaskFuture> finishedTaskFutureCache;

    Map<String, AsyncTaskFuture> doingTasks;

    public AsyncTaskManager(){

        finishedTaskFutureCache = CacheFactory.createLocalCache("AysncTaskFuture", 1 * JEConstants.HOUR, -1);



    }


}
