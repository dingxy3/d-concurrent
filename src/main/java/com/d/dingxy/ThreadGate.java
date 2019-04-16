package com.d.dingxy;

/**
 * @param
 * @Author: dingxy3
 * @Description:阀门类
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
