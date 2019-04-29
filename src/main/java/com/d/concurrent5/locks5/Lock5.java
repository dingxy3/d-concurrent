package com.d.concurrent5.locks5;

import java.util.concurrent.TimeUnit;

/**
 * ============================
 * @version [版本号, 2019/3/29]
 * @Auther: dingxy
 * @Description: this class equals jdk 's Lock ,It's mine Lock ,study from jdk
 * @since [产品/模块版本]
 * =============================
 */
public interface Lock5 {

    void lock();

    boolean tryLock();

    boolean tryLock(long times, TimeUnit unit) throws InterruptedException;

    void lockInterruptibly();

    void unlock();

    Condition5 newCondition5();
}
