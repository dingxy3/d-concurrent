package com.d.concurrent5.locks5;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * ============================
 * @version [版本号, 2019/3/29]
 * @Auther: dingxy
 * @Description:
 * @since [产品/模块版本]
 * =============================
 */
public class ReentrantLock5 implements Lock5 ,Serializable {

    private static final long serialVersionUID = 1L;

    private final Sync5 sync5;

    public ReentrantLock5() {
        sync5 = new NonfairSync5() ;
    }
    public ReentrantLock5(boolean fair){
      sync5 =  fair ? new NonfairSync5() : new FairSync5();
    }

    abstract static  class Sync5 extends AbstractQueuedSynchronizer{

        abstract void lock();

        /*非公平锁尝试获得锁*/
        final boolean nonfairTryAcquire(int acquires){
             /*获得当前线程*/
              final  Thread current = Thread.currentThread();
              /**/
              int i = getState() ;

              if (i == 0)
              {
                 if (compareAndSetState(0,acquires))
                 {
                     setExclusiveOwnerThread(current);
                     return true;

                 }
              }else
              {
                  if (current ==getExclusiveOwnerThread())
                  {
                      int n = i + acquires ;

                      if (n < 0)
                      {
                          throw new Error("Maxnuim lock count exceeded") ;

                      }
                      setState(n);
                      return  true ;

                  }

              }
              return false ;
        }
        /*释放锁*/
        @Override
        protected final boolean tryRelease(int release){

            int c = getState() - release ;

            if (Thread.currentThread() != getExclusiveOwnerThread())
            {

                throw new IllegalMonitorStateException();
            }
            if (c == 0){

                setExclusiveOwnerThread(null);
                return true ;

            }

            setState(c);
            return false ;

        }
        /*是否当前线程*/
        @Override
        protected  final boolean isHeldExclusively(){
            return Thread.currentThread() == getExclusiveOwnerThread();
        }
        /*return 一个condition*/
       final ConditionObject newContion(){
            return new ConditionObject() ;
       }
       /*获得当前持有的线程*/
       final Thread getOwner(){
           return  getState() == 0 ? null : getExclusiveOwnerThread();
       }
       /*当前线程持有的计数*/
      final int getHoldCount(){
           return  isHeldExclusively() ? getState() : 0 ;
       }
       /*是否上锁*/
       final boolean isLocked(){
          return getState() != 0 ;
       }
        /**
         * 从流中重新构造实例，即反序列化。
         */
        private void readObject(java.io.ObjectInputStream s)
                throws java.io.IOException, ClassNotFoundException {

            s.defaultReadObject();
            setState(0); // reset to unlocked state

        }
    }

    /**
     * 非公平锁
     */
    static final class NonfairSync5 extends Sync5{


        @Override
        void lock() {

            if (compareAndSetState(0,1))
            {
                setExclusiveOwnerThread(Thread.currentThread());

            }else
            {
                acquire(1);

            }
        }
        protected final boolean tryAcquire(int a){
            return nonfairTryAcquire(a);
        }
    }

    static final class FairSync5 extends Sync5{

        @Override
        void lock() {
                acquire(1);
        }

        protected final boolean tryAcquire(int a){

            final Thread t = Thread.currentThread() ;

            int c = getState() ;

            if (c == 0){
                if (compareAndSetState(0,a) && !hasQueuedPredecessors())
                {
                    setExclusiveOwnerThread(t);
                    return true ;

                }
            }else if(t == getExclusiveOwnerThread())
            {
                int n = c + a ;
                if (n < 0)
                {
                    throw new Error("Maximum lock count exceeded");
                }
                setState(n);
                return true ;

            }
            return false;

        }
    }

    public void lock() {
        sync5.lock();
    }

    public boolean tryLock() {
        return sync5.nonfairTryAcquire(1);
    }

    public boolean tryLock(long times, TimeUnit unit) throws InterruptedException {
        return sync5.tryAcquireNanos(1,unit.toNanos(times));
    }

    public void lockInterruptibly() {

    }

    public void unlock() {
        sync5.tryRelease(1) ;
    }

    public Condition5 newConditon5() {

       // return sync5.newContion() ;
        return null ;
    }
}
