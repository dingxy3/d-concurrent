package com.d.dingxy;

/**
 * @param
 * @Author: dingxy3
 * @Description:阀门类,使用条件谓词相当于状态，Object
 *方法的wait() notifyAll()
 * @Date: Created in  2019/4/16
 **/
public class ThreadGate {

    private boolean isOpen ;

    private  int generation ;

    public synchronized void open(){
        ++ generation ;

        isOpen = true ;

        notifyAll();
    }

    public  synchronized void  close(){

        isOpen = false ;
    }

    public synchronized void await() throws InterruptedException {
        int a = generation ;
        if (!isOpen && a == generation)
        {
            wait();
        }
    }

}
