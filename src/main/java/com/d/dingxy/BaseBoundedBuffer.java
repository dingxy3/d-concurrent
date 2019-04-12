package com.d.dingxy;

/**
 * @param
 * @Author: dingxy3
 * @Description:有界缓存基类，类似组队列，为空take不了，满了put不了
 * @Date: Created in  2019/4/10
 **/
public abstract class BaseBoundedBuffer<V> {


    private final V[] buff ;
    private  int head ;
    private int tail ;
    private volatile int count ;

    public  BaseBoundedBuffer(int capacity){
        buff = (V[]) new Object[capacity];
    }

    protected   synchronized  final void doPut(V v){
        buff[tail] = v;
        if (++tail == buff.length)
        {
            tail = 0 ;
        }
        ++count ;
    }

    protected  synchronized final V  doTake(){
            V v = buff[head] ;
            buff[head] =null ;
            if (++head == buff.length){
                head = 0 ;
            }
            --count ;
            return v;
    }

    /**
     * 队列是否满了
     * @return
     */
    protected synchronized final boolean isFull(){

        return buff.length == count ;
    }

    /**
     * 队列是否空的
     * @return
     */
    protected synchronized final boolean isEmpty(){
        return  count == 0 ;
    }

}