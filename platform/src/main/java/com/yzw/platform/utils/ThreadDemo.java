package com.yzw.platform.utils;

import java.util.concurrent.Semaphore;

/**
 * 需求现在有100张火车票，有两个窗口同时抢火车票，请使用多线程模拟抢票效果。
 * Created by yz on 2018/04/01.
 */
public class ThreadDemo {

    private Semaphore semaphore;

    public static void main(String[] args) {
        // t1 t2同时共享同一变量trainCount
        ThreadTrain threadTrain = new ThreadTrain();
        Thread t1 = new Thread(threadTrain, "窗口1");
        Thread t2 = new Thread(threadTrain, "窗口2");
        Thread t3 = new Thread(threadTrain, "窗口3");
        Thread t4 = new Thread(threadTrain, "窗口4");
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}

    // 售票窗口
    class ThreadTrain implements Runnable{
        // 总共有100张火车票
        private int trainCount = 100;
        public void run() {
            while (trainCount > 0){
                /*try {
                    // 休眠50秒
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                // 出售火车票
                sale();
            }
        }

        // 卖票方法
        public void sale(){
            synchronized (this) {
                if (trainCount > 0) {
                    System.out.println(Thread.currentThread().getName() + ",出售第" + (100 - trainCount + 1) + "张票");
                    trainCount--;
                }
            }
        }
    }
/*

class MyTask implements Runnable {

    private Semaphore semaphore; // 信号量
    private int user; // 第几个用户

    public MyTask(Semaphore semaphore, int user) {
        this.semaphore = semaphore;
        this.user = user;
    }

    @Override
    public void run() {
        try {
            // 获取信号量许可，才能占用窗口
            semaphore.acquire();
            // 运行到这里说明获取到了许可，可以去买票了
            System.out.println("用户" + user + "进入窗口，准备买票...");
            Thread.sleep((long) Math.random() * 10000); // 模拟买票时间
            System.out.println("用户" + user + "买票完成，准备离开...");
            Thread.sleep((long) Math.random() * 10000);
            System.out.println("用户" + user + "离开售票窗口...");
            // 释放信号量许可证
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}*/
