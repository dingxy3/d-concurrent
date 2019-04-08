### 并发编程---源码类逐一分析

### 前言
```
在java开发中，常常需要用到锁这一类东西，
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
第二个实现了公平锁和非公平锁的分离，也是一种多态的表现吧，单一职责工厂方法

具体怎么实现公平和非公平2个构造方法，默认非公平，传boolean
```