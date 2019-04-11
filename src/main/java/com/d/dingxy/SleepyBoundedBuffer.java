package com.d.dingxy;

/**
 * @param
 * @Author: dingxy3
 * @Description:通过轮询与休眠来实现简单的阻塞
 * @Date: Created in  2019/4/11
 **/
public class SleepyBoundedBuffer<V> extends BaseBoundedBuffer<V> {

    public SleepyBoundedBuffer(int capacity){
        super(capacity);
    }

    //2s
    private static final long SLEEP_GRANULARITY = 2000;



    public void  put(V v) throws InterruptedException {
        while (true)
        {
            synchronized (this)
            {
                if (!isFull())
                {
                    doPut(v);
                    return;
                }
            }
            Thread.sleep(SLEEP_GRANULARITY);
        }

    }

    public V take() throws InterruptedException {

        while (true)
        {
            synchronized (this)
            {
               if(!isEmpty())
               {
                   doTake();
               }
            }
            Thread.sleep(SLEEP_GRANULARITY);
        }
    }
}
