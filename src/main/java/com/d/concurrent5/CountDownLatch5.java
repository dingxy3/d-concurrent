package com.d.concurrent5;

import com.d.concurrent5.locks5.AbstractQueuedSynchronizer5;

/**
 * ============================
 *
 * @version [版本号, 2019/4/10]
 * @Auther: dingxy
 * @Description:线程汇聚，阻塞当前主线程，让其他线程先执行完。共享锁
 * @since [产品/模块版本]
 * =============================
 */
public class CountDownLatch5 {

    private  Sync syn ;

    /**
     * 构造方法初始化CountDownLatch
     * @param count
     */
    public CountDownLatch5(int count){syn = new Sync(count);}

    private final static class  Sync extends AbstractQueuedSynchronizer5{


        /**
         * AQS的state作为初始化计数器，即线程的数量
         * @param count
         */
        Sync(int count){
           setState(count);
        }

        /**
         * 获得还剩余的线程数量
         * @return
         */
        int getCount(){
          return  getState();
       }

        @Override
        protected int tryAcquireShared(int arg) {
           //
           return ( getState() == 0 ) ? 1 : -1 ;
        }

        /*释放锁通过compareAndSwap*/
        @Override
        protected boolean tryReleaseShared(int arg) {
            //循环进行cas，直到当前线程成功完成cas使计数值（状态值state）减一并更新到state
            for (;;) {
                //获得当前计数器的数目
                int c = getState();

                //如果计数器为0
                if (c == 0)
                {
                    //返回false
                    return false;
                }

                int nextc = c-1;

                //CAS设置计数值减一
                if (compareAndSetState(c, nextc))
                {
                    //最后一个线程才会走到这一步返回true，然后才会进入AQS的doReleaseShared()
                    //方法释放主线程锁
                    return nextc == 0;
                }
            }
        }
    }

    /**
     * 计数器减1
     */
    public void  countDown(){
        syn.releaseShared(1);
    }

    public void await() throws InterruptedException {
       syn.acquireSharedInterruptibly(1) ;
    }
}
