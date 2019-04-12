### 并发编程---源码类逐一分析

### 前言
```
在java开发中，常常需要用到锁这一类东西，在理解ReentrantLock之前需要了解
AbstractQueuedSynchronizer的一些原理
```

### 背景
#### ReentrantLock源码剖析

##### ReentrantLock几个重要代码结构
```
ReentrantLock
       抽象内部类Sync继承AQS
           静态子类NonFairSync
           静态子类FairSync
        上面为策略模式
        void lock()
        boolean tryLock()
        boolean  tryLoct(long,TimeUnit)
        void lockInterruptibly()
        2个构造方法：
          ReentrantLock()
          ReentrantLock(boolean)


```
##### 源码解读

```
Sync以抽象内部类的身份在ReenTrantLock出现
解决了2个重要的问题。
第一个继承了AQS拿到了AQS中的方法
第二个实现了公平锁和非公平锁的分离，策略模式

具体怎么实现公平和非公平2个构造方法，默认非公平，传boolean
```

####AbstractQueuedSynchronizer5
### 前言
```
在谈AbstractQueuedSynchronizer的源码实现前，需要了解几点重要知识点
第一点同步器状态volatile，第二点cas ，第三点队列使用，第四点Unsafe类 
```

### 背景


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
```


