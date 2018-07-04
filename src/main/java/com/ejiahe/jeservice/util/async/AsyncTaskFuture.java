package com.ejiahe.jeservice.util.async;

import java.util.concurrent.Future;

public interface AsyncTaskFuture<T> extends Future<T> {


    /**
     * 获取进度百分比;
     * <p>
     *     <li>未开始： 0</li>
     *     <li>进行中: 1-99</li>
     *     <li>已完成或终止: 100</li>
     * </p>
     * @return
     */
    int getPercentage();



}
