package com.d.concurrent5;

import com.d.concurrent5.locks5.Condition5;
import com.d.concurrent5.locks5.ReentrantLock5;

import java.util.concurrent.locks.Condition;

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
    private final Condition trip = lock.newCondition5();

    /** 拦截线程数*/
    private final int parties;

    /** 跳闸时要执行的线程数*/
    private final Runnable barrierCommand;

    /*构造函数初始化栅栏*/
    public CyclicBarrier5(int parties, Runnable barrierAction) {
        if (parties <= 0) throw new IllegalArgumentException();
        this.parties = parties;
        this.count = parties;
        this.barrierCommand = barrierAction;
    }
    public CyclicBarrier5(int parties) {
        this(parties, null);
    }

    private Generation5 generation = new Generation5();


    private static class Generation5{
        boolean broken = false;
    }
    private int count;


    private void nextGeneration() {
        // signal completion of last generation
        trip.signalAll();
        // set up next generation
        count = parties;
        generation = new Generation5();
    }

    private void breakBarrier() {
        generation.broken = true;
        count = parties;
        trip.signalAll();
    }
}
