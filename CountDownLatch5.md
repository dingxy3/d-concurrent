#### 前言
```
如果你刚接触CountDownLatch：什么是CountDownLatch？ CountDownLatch是干嘛的？怎么使用CountDownLatch?
如果你会使用CountDownLatch：CountDownLatch是怎么阻塞当前线程的？
如果你能熟练使用CountDownLatch： CountDownLatch是怎么设计的？
```

#### 背景
```
Doug Lea 大神在 JUC 包中为我们准备了大量的多线程工具，其中包括 CountDownLatch ，
名为倒计时门栓或者线程汇聚，本文档旨在搞懂以上几个问题。

```
```
主线程被阻塞在await()方法处，直到所有其他的线程调用完countdown()后，到达await()
主线程才可是执行
```
#### CountDownLatch使用
```java
class Drive2 {

  public static void main(String[] args) throws InterruptedException {
    CountDownLatch doneSignal = new CountDownLatch(10);
    Executor e = Executors.newFixedThreadPool(10);
    for (int i = 0; i < 10; ++i) {
      e.execute(new WorkerRunnable(doneSignal, i));
    }

    doneSignal.await();           // 当执行到这儿主线程被阻塞，等待所有线程执行完
    System.err.println("work");
  }
}

class WorkerRunnable implements Runnable {

  private final CountDownLatch doneSignal;
  private final int i;

  WorkerRunnable(CountDownLatch doneSignal, int i) {
    this.doneSignal = doneSignal;
    this.i = i;
  }

  public void run() {
    doWork(i);
    doneSignal.countDown();
  }

  void doWork(int i) {
    System.out.println("work"+i);
  }
}
```


#### CountDownLatch代码结构
     静态内部类Sync 继承AQS，重写了几个方法
     await()方法
     countDown()方法
     