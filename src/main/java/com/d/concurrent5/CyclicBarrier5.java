package com.d.concurrent5;

import com.d.concurrent5.locks5.Condition5;
import com.d.concurrent5.locks5.ReentrantLock5;

/**
 * ============================
 *
 * @version [版本号, 2019/4/12]
 * @Auther: dingxy
 * @Description:线程栅栏（线程屏障）人满发车
 * @since [产品/模块版本]
 * =============================
 */
public class CyclicBarrier5 {


    /** 栅栏入口锁*/
    private final ReentrantLock5 lock = new ReentrantLock5();

    /**待跳闸状态 */
    private final Condition5 trip = lock.newCondition5();

}
