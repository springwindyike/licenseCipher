package com.ejiahe.jeservice.context;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by mjf on 2016/12/17.
 */
public interface PluggableBean extends InitializingBean, DisposableBean{

    /**
     * 系统启动后触发;
     */
    void applicationStarted();

}
