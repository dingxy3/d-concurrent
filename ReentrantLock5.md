### 前言
```
ReentranLock只支持独占方式的获取操作,内部类Sync集成AQS,并且重写了
tryAcquire()->lock
tryRelease()->方法
isHeldExclusively()
```

### 背景
#### ReentrantLock源码剖析
```
在java开发中，常常需要用到锁这一类东西，在理解ReentrantLock之前需要了解
AbstractQueuedSynchronizer的一些原理，是基于aqs的state为0时表示锁被占用，为1时表示锁被释放
```

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
          ReentrantLock()
          ReentrantLock(boolean)
          公平锁lock()方法是有队列，hasQueueProced（），而非公平锁，直接去抢占没有队列这一环
          