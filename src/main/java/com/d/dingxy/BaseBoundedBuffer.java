package com.d.dingxy;

/**
 * @param
 * @Author: dingxy3
 * @Description:条件谓词 条件队列 锁  有界阻塞队列
 * @Date: Created in  2019/4/10
 **/
public abstract class BaseBoundedBuffer<V> {


    private final V[] buff ;
    private  int head ;
    private int tail ;
    private  int count ;

    public  BaseBoundedBuffer(int capacity){
        buff = (V[]) new Object[capacity];
    }

    protected   synchronized  final V doTake(){
        V v = buff[head];

        return v ;
    }

    protected  synchronized final void  doPut(){

    }

    private synchronized final boolean isFull(){

        return buff.length == count ;
    }
    private synchronized final boolean isEmpty(){
        return  count == 0 ;
    }

}