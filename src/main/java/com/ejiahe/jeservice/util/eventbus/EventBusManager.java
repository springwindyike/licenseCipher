package com.ejiahe.jeservice.util.eventbus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件总线管理器, 该类维持了本地JVM中的所有系统事件总线的生命周期;
 * <p>
 * 该类提供了便捷的方法, 供上层在默认事件总线上注册/反注册监听器, 还有分发事件;
 *
 * @author MaiJingFeng
 */
public class EventBusManager {

    private Map<String,EventBus> buses = new ConcurrentHashMap<String,EventBus>();

    private EventBus default_eventBus = new EventBus();

    private static EventBusManager ebManager = new EventBusManager();

    private EventBusManager(){

    }

    public static EventBusManager getInstance(){
        return ebManager;
    }

    /**
     * 获取指定标识的事件总线
     * @param eventBusName 事件总线标识名
     * @param threadPoolSize 事件总线异步分发时的最大并发数
     * @return
     *
     */
    public EventBus getEventBus(String eventBusName, int threadPoolSize){
        if(eventBusName == null) return getEventBus();

        EventBus bus = buses.get(eventBusName);
        if(bus == null){
            EventBus temp = new EventBus(eventBusName,threadPoolSize);
            bus = temp;
            buses.put(eventBusName, bus);
        }

        return bus;
    }

    /**
     * 获取缺省的系统事件总线
     * @return
     *
     */
    public EventBus getEventBus(){
        return default_eventBus;
    }

    /**
     * 在默认事件总线上添加事件监听器
     * @see EventBus
     * @param listener 标注有@Subscribe的监听器
     *
     */
    public void addListener(Object listener){
        getEventBus().addListener(listener);
    }

    /**
     * 在默认事件总线上移除事件监听器
     * @param listener
     *
     */
    public void removeListener(Object listener){
        getEventBus().removeListener(listener);
    }

    /**
     * 在事件总线上同步发布任意事件;
     * <br/>
     * note:该方法会等待事件分发执行完毕后返回
     * @param event
     *
     */
    public void post(Object event){
        getEventBus().post(event);
    }

    /**
     * 在事件总线上异步发布任意事件;
     * <br/>
     * note:该方法会马上返回
     * @param event
     *
     */
    public void asyncPost(Object event){
        getEventBus().asyncPost(event);
    }


}