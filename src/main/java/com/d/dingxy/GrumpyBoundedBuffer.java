package com.d.dingxy;

/**
 * ============================
 *
 * @version [版本号, 2019/4/11]
 * @Auther: dingxy
 * @Description:对插入和获取元素操作进行先行检查，然后执行操作，校验不通过不予操作
 * @since [产品/模块版本]
 * =============================
 */
public class GrumpyBoundedBuffer<V> extends BaseBoundedBuffer<V> {

    public  GrumpyBoundedBuffer(int capacity){
        super(capacity);
    }

    public synchronized void put(V v){
        if (!isFull())
        {
            doPut(v);
        }
    }

    public synchronized  V take(){
        if (!isEmpty())
        {
           return doTake();
        }
        return  null ;
    }
}
