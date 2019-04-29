package com.d.concurrent5.locks5;

import sun.misc.Unsafe;

import java.io.Serializable;
import java.util.concurrent.locks.LockSupport;

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

    private transient volatile  int state ;

    private static final Unsafe unsafe = Unsafe.getUnsafe();

    /**
     * 读锁共享锁
     * @param arg
     * @return
     */
    protected int tryAcquireShared(int arg) {
        throw new UnsupportedOperationException();
    }
    /*释放锁通过compareAndSwap*/
    protected boolean tryReleaseShared(int arg) {
        throw new UnsupportedOperationException();
    }

    protected final int getState() {
        return state;
    }

    protected final  void setState(int newState){
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

        /**
         * 返回前驱节点
         * @return
         * @throws NullPointerException
         */
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

    static final long spinForTimeoutThreshold = 1000L;

    /**
     * CAS head field. Used only by enq.
     */
    private final boolean compareAndSetHead(Node5 update) {
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }

    private final boolean compareAndSetTail(Node5 expect, Node5 update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }
    private static final boolean compareAndSetWaitStatus(Node5 node,
                                                         int expect,
                                                         int update) {
        return unsafe.compareAndSwapInt(node, waitStatusOffset,
                expect, update);
    }

    public static void main(String[] args) throws NoSuchFieldException {
        System.out.println(unsafe.objectFieldOffset
                (AbstractQueuedSynchronizer5.class.getDeclaredField("state")));
    }
    /**
     * 很经典的一个套路，将当前节点加入队列尾端
     * @param node
     * @return
     */
    private Node5 enq(final Node5 node) {
          for (;;)
          {
              Node5 t = tail ;
              if (t == null){
                  if (compareAndSetHead(new Node5())){
                      tail = head ;
                  }
              }else
              {
                  node.prev = tail ;
                  if (compareAndSetTail(t,node)){
                      t.next = node ;
                      return  t ;
                  }
              }
          }
    }


    /**
     * 加入队列
     * @param node
     * @return
     */
    private Node5 addWaiter(Node5 node){
       Node5 t = new Node5(Thread.currentThread(),node) ;
       Node5 pred  = tail ;
       if (pred != null)
       {
         node.prev = pred ;
         if (compareAndSetTail(pred ,node))
         {
             pred.next = node ;
             return  node ;
         }
       }
       enq(node) ;

       return  node ;
    }

    /**
     * 设置头节点
     * @param node
     */
    private void setHead(Node5 node){
        head = node ;
        node.thread =null ;
        node.prev = null ;
    }

    private void unparkSuccessor(Node5 node) {

        int ws = node.waitStatus;

        if (ws < 0)
        {
            compareAndSetWaitStatus(node,ws,0) ;
        }
        Node5 s = node.next ;
        if (s == null || s.waitStatus > 0)
        {
            s = null ;
            for (Node5 t = tail ; t != null && t != node ; t =t.prev )
            {
                if(t.waitStatus <= 0)
                {
                    s = t ;
                }
            }
            if (s != null)
            {
                LockSupport.unpark(s.thread);
            }
        }
    }

    public final boolean releaseShared(int arg) {
        if (tryReleaseShared(arg)) {
            doReleaseShared();
            return true;
        }
        return false;
    }

    private void doReleaseShared() {

        for (;;)
        {
            Node5 h = head;
            if (h != null && h != tail)
            {
                int ws = h.waitStatus;
                if (ws == Node5.SIGNAL)
                {
                    if (!compareAndSetWaitStatus(h, Node5.SIGNAL, 0))
                        continue;            // loop to recheck cases
                    unparkSuccessor(h);
                }
                else if (ws == 0 &&
                        !compareAndSetWaitStatus(h, 0, Node5.PROPAGATE))
                    continue;                // loop on failed CAS
            }
            if (h == head)                   // loop if head changed
                break;
        }
    }

    public final void acquireSharedInterruptibly(int arg)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        if (tryAcquireShared(arg) < 0)
            doAcquireSharedInterruptibly(arg);
    }
    private void setHeadAndPropagate(Node5 node, long propagate) {
        Node5 h = head; // Record old head for check below
        setHead(node);

        if (propagate > 0 || h == null || h.waitStatus < 0 ||
                (h = head) == null || h.waitStatus < 0) {
            Node5 s = node.next;
            if (s == null || s.isShared())
                doReleaseShared();
        }
    }

    private static boolean shouldParkAfterFailedAcquire(Node5 pred, Node5 node) {
        int ws = pred.waitStatus;
        if (ws == Node5.SIGNAL)

            return true;
        if (ws > 0) {

            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {

            compareAndSetWaitStatus(pred, ws, Node5.SIGNAL);
        }
        return false;
    }

    private void doAcquireSharedInterruptibly(int arg)
            throws InterruptedException {
        final Node5 node = addWaiter(Node5.SHARED);
        boolean failed = true;
        try {
            for (;;) {
                final Node5 p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        failed = false;
                        return;
                    }
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this);
        return Thread.interrupted();
    }

    private void cancelAcquire(Node5 node) {
        // Ignore if node doesn't exist
        if (node == null)
            return;

        node.thread = null;

        // Skip cancelled predecessors
        Node5 pred = node.prev;
        while (pred.waitStatus > 0)
            node.prev = pred = pred.prev;

        // predNext is the apparent node to unsplice. CASes below will
        // fail if not, in which case, we lost race vs another cancel
        // or signal, so no further action is necessary.
        Node5 predNext = pred.next;

        // Can use unconditional write instead of CAS here.
        // After this atomic step, other Nodes can skip past us.
        // Before, we are free of interference from other threads.
        node.waitStatus = Node5.CANCELLED;

        // If we are the tail, remove ourselves.
        if (node == tail && compareAndSetTail(node, pred)) {
            compareAndSetNext(pred, predNext, null);
        } else {
            // If successor needs signal, try to set pred's next-link
            // so it will get one. Otherwise wake it up to propagate.
            int ws;
            if (pred != head &&
                    ((ws = pred.waitStatus) == Node5.SIGNAL ||
                            (ws <= 0 && compareAndSetWaitStatus(pred, ws, Node5.SIGNAL))) &&
                    pred.thread != null) {
                Node5 next = node.next;
                if (next != null && next.waitStatus <= 0)
                    compareAndSetNext(pred, predNext, next);
            } else {
                unparkSuccessor(node);
            }

            node.next = node; // help GC
        }
    }
    private static final boolean compareAndSetNext(Node5 node,
                                                   Node5 expect,
                                                   Node5 update) {
        return unsafe.compareAndSwapObject(node, nextOffset, expect, update);
    }


    public class ConditionObject implements Condition5, java.io.Serializable {

        @Override
        public void await() {

        }

        @Override
        public void singal() {

        }
    }
}
