package com.d.dingxy;

/**
 * @param
 * @Author: dingxy3
 * @Description:条件队列 同BlockQueue
 * @Date: Created in  2019/4/11
 **/
public class BoundedBuffer<v> extends  BaseBoundedBuffer<V> {

    public BoundedBuffer(int capacity){
        super(capacity);
    }
}
