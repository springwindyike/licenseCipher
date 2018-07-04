package com.ejiahe.jeservice.util.concurrent;

/**
 * Created by mjf on 2016/12/18.
 */
public interface Callback<T> {

    void call(T object);

}
