package com.d.concurrent5;

import com.d.concurrent5.locks5.AbstractQueuedSynchronizer5;

/**
 * ============================
 *
 * @version [版本号, 2019/4/10]
 * @Auther: dingxy
 * @Description:信号量，每次只允许部分线程干事
 * @since [产品/模块版本]
 * =============================
 */
public class Semaphore5 {

    private Syn syn ;

    /**
     * 设置允许的线程数
     * @param permits
     */
    public Semaphore5(int permits){ syn = new NonFair(permits) ;}

    /**
     * 设置允许的线程数，并且可以提供公平非公平锁
     * @param permits
     * @param fair
     */
    public  Semaphore5(int permits,boolean fair){ syn = fair ? new Fair(permits) : new NonFair(permits) ; }

    static abstract class Syn extends AbstractQueuedSynchronizer5{

         Syn(int permits){ setState(permits); }

        final int nonfairTryAcquireShared(int acquires) {

        }
    }


    static final class  Fair extends  Syn{
         Fair(int permits){ super(permits); }

        protected int tryAcquireShared(int acquries) {
               return  nonfairTryAcquireShared(acquries) ;
        }

    }
    static final class NonFair extends  Syn{

         NonFair(int permits ){ super(permits);}

    }

    /**
     * 获得允许
     * @throws InterruptedException
     */
    public void  acquire() throws InterruptedException {
        syn.acquireSharedInterruptibly(1);
    }

    /**
     * 获得一定的线程允许
     * @param permits
     * @throws InterruptedException
     */
    public  void acquire(int permits) throws InterruptedException {
        if (permits < 0) throw  new IllegalArgumentException();

        syn.acquireSharedInterruptibly(permits);

    }
}
