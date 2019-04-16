####AbstractQueuedSynchronizer5
### 前言
```
在谈AbstractQueuedSynchronizer的源码实现前，需要了解几点重要知识点
第一点同步器状态volatile，第二点cas ，第三点队列使用，第四点Unsafe类 
```

### 背景
java中许多阻塞的类，如ReentrantLock、Semphore、CountDownLatch、

FutureTask、ReentrantReadWriteLock、SynchronousQueue都是基于AQS构建的

###

1、状态同步state，volatile修饰，保证可见性。
该字段为同步器的状态控制，而队列是保存线程的池子，通过该状态的判断是否能获得锁，就是所谓的条件谓词
可以参考com.d.dingxy.BoundedBuffer类，实现了锁的语意，只有满足我的状态你才能拿到锁，才能进入等待队列，
这是充要条件

这个状态在同步队列中的设计是：给实现他的几个锁自己去控制状态加减 ，逻辑交给外部，自己只是提供
这个状态存放和队列操作。比如CountDownLatch、Semaphore都是这样去设计的
```
java.util.concurrent.locks.AbstractQueuedSynchronizer.getState()
java.util.concurrent.locks.AbstractQueuedSynchronizer.setState(int)
java.util.concurrent.locks.AbstractQueuedSynchronizer.compareAndSetState(int, int)