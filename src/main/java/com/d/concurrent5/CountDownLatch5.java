package com.d.concurrent5;

import com.d.concurrent5.locks5.AbstractQueuedSynchronizer5;

/**
 * ============================
 *
 * @version [版本号, 2019/4/10]
 * @Auther: dingxy
 * @Description:
 * @since [产品/模块版本]
 * =============================
 */
public class CountDownLatch5 {

    private  Sync syn ;

    public CountDownLatch5(int count){syn = new Sync(count);}

    private final static class  Sync extends AbstractQueuedSynchronizer5{


        Sync(int count){
           setState(count);
        }

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
            for (;;) {
                int c = getState();
                if (c == 0) {
                    return false;
                }
                int nextc = c-1;
                if (compareAndSetState(c, nextc)) {
                    return nextc == 0;
                }
            }
        }
    }

    public void  countDown(){
        syn.releaseShared(1);
    }

    public void await() throws InterruptedException {
       syn.acquireSharedInterruptibly(1) ;
    }
}
