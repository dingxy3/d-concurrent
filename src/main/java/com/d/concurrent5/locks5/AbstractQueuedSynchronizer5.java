package com.d.concurrent5.locks5;

import sun.misc.Unsafe;

import java.io.Serializable;

/**
 * ============================
 *
 * @version [版本号, 2019/4/2]
 * @Auther: dingxy
 * @Description:AQS
 * @since [产品/模块版本]
 * =============================
 */
public abstract class AbstractQueuedSynchronizer5 extends AbstractOwnableSynchronizer5 implements Serializable{
   public static final long  serialVersionUID = 1L;

    private transient volatile Node5 head ;

    private transient volatile Node5 tail ;

    private transient volatile  long state ;

    private static final Unsafe unsafe = Unsafe.getUnsafe();


    protected final long getState() {
        return state;
    }

    protected final  void setState(long newState){
        state = newState ;
    }

    protected AbstractQueuedSynchronizer5() { }

    static final class Node5{
        /*共享模式*/
        static final Node5 SHARED = new Node5();

        /*独占模式*/
        static final Node5 EXCLUSIVE = null;

        static final int CANCELLED =  1;

        static final int SIGNAL    = -1;

        static final int CONDITION = -2;

        static final int PROPAGATE = -3;

        volatile int waitStatus;

        volatile Node5 prev;

        volatile Node5 next;

        volatile Thread thread;

        Node5 nextWaiter;

        final boolean isShared() {
            return nextWaiter == SHARED;
        }

        final Node5 predecessor() throws NullPointerException{
            Node5 p = prev ;
            if (p == null)
            {
                throw new NullPointerException() ;
            }else
            {
                return  p ;
            }

        }
        Node5(){}

        Node5(Thread thread, Node5 mode) {     // Used by addWaiter
            this.nextWaiter = mode;
            this.thread = thread;
        }

        Node5(Thread thread, int waitStatus) { // Used by Condition
            this.waitStatus = waitStatus;
            this.thread = thread;
        }
    }

    private static final long stateOffset;
    private static final long headOffset;
    private static final long tailOffset;
    private static final long waitStatusOffset;
    private static final long nextOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer5.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer5.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer5.class.getDeclaredField("tail"));
            waitStatusOffset = unsafe.objectFieldOffset
                    (Node5.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset
                    (Node5.class.getDeclaredField("next"));

        } catch (Exception ex) { throw new Error(ex); }
    }

    protected final  boolean compareAndSetState(long expect ,long update){
      return   unsafe .compareAndSwapLong(this,stateOffset,expect,update) ;
    }
}
