package com.d.concurrent5.locks5;

import java.io.Serializable;

/**
 * ============================
 *
 * @version [版本号, 2019/4/2]
 * @Auther: dingxy
 * @Description:
 * @since [产品/模块版本]
 * =============================
 */
public abstract class AbstractOwnableSynchronizer5 implements Serializable{

    private static final long serialVersionUID = 1L;

    protected AbstractOwnableSynchronizer5(){}

    /**
     * 当前线程的持有者，独享模式的同步
     */
    private transient Thread exclusiveOwnerThread ;

}
