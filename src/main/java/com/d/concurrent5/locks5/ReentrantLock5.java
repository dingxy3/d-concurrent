package com.d.concurrent5.locks5;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * ============================
 * @version [版本号, 2019/3/29]
 * @Auther: dingxy
 * @Description:
 * @since [产品/模块版本]
 * =============================
 */
public class ReentrantLock5 implements Lock5 ,Serializable {

    private static final long serialVersionUID = 1L;


    static final class Node5{

        /*共享模式node*/
        static final Node5 SHARED = new Node5();

        /*独占模式Node*/
        static final Node5 EXCLUSIVE = null;

        /*线程取消状态值*/
        static final int CANCELLED =  1;
         /**后续线程不需要等待*/
        static final int SIGNAL    = -1;
        /*线程在Condition上等待*/
        static final int CONDITION = -2;

        /*传播*/
        static final int PROPAGATE = -3;

        volatile int waitStatus;

        volatile Node5 prev;

        volatile Node5 next;

        private transient volatile Node5 head;

        private transient volatile Node5 tail;

        private volatile int state;

        volatile Thread thread;

        Node5 nextWaiter;

        protected final int getState(){
            return state ;
        }

        protected final void setState(int s){
            state =s ;
        }

        final boolean isShared(){  return  nextWaiter == SHARED ; }


    }

    abstract static  class Sync5 extends AbstractQueuedSynchronizer{

        abstract void lock();

        final boolean nonfairTryAcquire(int acquires){
              final  Thread current = Thread.currentThread();
              int i = getState() ;
              if (i == 0)
              {

              }
        }
    }


    public void lock() {

    }

    public boolean tryLock() {
        return false;
    }

    public boolean tryLock(long times, TimeUnit unit) {
        return false;
    }

    public void lockInterruptibly() {

    }

    public void unlock() {

    }

    public Condition5 newConditon5() {
        return null;
    }
}
