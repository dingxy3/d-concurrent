package com.d.dingxy;

/**
 * @param
 * @Author: dingxy3
 * @Description:条件队列 同BlockQueue
 * @Date: Created in  2019/4/11
 **/
public class BoundedBuffer<V> extends  BaseBoundedBuffer<V> {

    public BoundedBuffer(int capacity){
        super(capacity);
    }

    public void put(V v) throws InterruptedException {
          while (isFull()){
              wait();
          }
          doPut(v);
          notifyAll();
    }

    public  V take(V v) throws InterruptedException {
        while (isEmpty()){
            wait();
        }
        V v1 =  doTake() ;
        notifyAll();
        return v1;
    }
}
