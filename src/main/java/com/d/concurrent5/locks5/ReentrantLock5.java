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
    /*策略模式根据入参new不同的锁*/
    public ReentrantLock5(boolean fair){
      sync5 =  fair ? new NonfairSync5() : new FairSync5();
    }

    abstract static  class Sync5 extends AbstractQueuedSynchronizer{
        /**
         * 策略模式获得公平锁还是非公平锁
         */
        abstract void lock();

        /*非公平锁尝试获得锁*/
        final boolean nonfairTryAcquire(int acquires){
             /*获得当前线程*/
              final  Thread current = Thread.currentThread();
              /*获得AQS的状态，看看是否锁被占用*/
              int i = getState() ;
             /*如果锁没有被占用*/
              if (i == 0)
              {
                 if (compareAndSetState(0,acquires))//cas比较并且替换原值。
                 {
                     /*设置当前线程为持有锁的线程*/
                     setExclusiveOwnerThread(current);
                     return true;

                 }
              }else
              {
                  if (current == getExclusiveOwnerThread())//如果当前线程等于锁持有线程（可重入锁）
                  {
                      int n = i + acquires ;//锁状态加1

                      if (n < 0)
                      {
                          throw new Error("Maxnuim lock count exceeded") ;

                      }
                      /*设置锁状态为n*/
                      setState(n);
                      return  true ;

                  }

              }
              return false ;
        }
        /*释放锁*/
        @Override
        protected final boolean tryRelease(int release){

            //获得当前锁的状态并且减去
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
            /*比较并替换当前*/
            if (compareAndSetState(0,1))
            {
                /*将当前线程设置为持有线程独占线程*/
                setExclusiveOwnerThread(Thread.currentThread());

            }else
            {
                /*尝试获取锁，如果没获得一直阻塞在队列（自旋获取排他锁）里直到获得锁*/
                acquire(1);

            }
        }
        @Override
        protected final boolean tryAcquire(int a){

            return nonfairTryAcquire(a);
        }
    }

    static final class FairSync5 extends Sync5{

        @Override
        void lock() {
                //获取锁，如果没获得一直阻塞在队列里直到获得锁*/
                acquire(1);
        }

        @Override
        protected final boolean tryAcquire(int a){

            ///获得当前线程
            final Thread t = Thread.currentThread() ;

            //获得锁的状态
            int c = getState() ;
            //如果当前锁没有被任何线程占有
            if (c == 0){
                //并判断当前线程是否在队列的队首，也就是是否有等待更久的线程。如果是队首返回ture并执行cas操作
                /*hasQueuedPredecessors是实现公平锁的保证*/
               // if (compareAndSetState(0,a) && !hasQueuedPredecessors())错误
                if ( !hasQueuedPredecessors() && compareAndSetState(0,a) )
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

    /**
     * 获取锁
     */
    @Override
    public void lock() {
        sync5.lock();
    }

    @Override
    public boolean tryLock() {
        return sync5.nonfairTryAcquire(1);
    }

    @Override
    public boolean tryLock(long times, TimeUnit unit) throws InterruptedException {
        return sync5.tryAcquireNanos(1,unit.toNanos(times));
    }

    @Override
    public void lockInterruptibly() {

    }

    @Override
    public void unlock() {
        sync5.release(1) ;
    }

    @Override
    public Condition5 newConditon5() {

       // return sync5.newContion() ;
        return null ;
    }
}
