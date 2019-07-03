package com.yzw.platform.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Slf4j
public class SemaphoreDemo {

    private int count=10;

    class MyTask implements Runnable{

        private Semaphore semaphore; // 信号量
        private int user;  // 第几个用户

        public MyTask(Semaphore semaphore, int user) {
            this.semaphore = semaphore;
            this.user = user;
        }

        @Override
        public void run() {

            try {
                if (count <= 0) {
                    log.info(">>>>>>>>>>票已售完，用户"+ user + "没买到票...<<<<<<<<<<");
                    return;
                }
                int queueLength = semaphore.getQueueLength();
                if (queueLength >= count) {
                    log.info(">>>>>>>>>>当前排队人数"+queueLength+"，剩余票数"+count+"，用户"+ user + "没买到票...<<<<<<<<<<");
                    return;
                }
                // 获取信号量许可，才能占用窗口
                log.info(">>>>>>>>>>剩余票数："+count+".用户"+ user + "开始排队获取信号量...<<<<<<<<<<");
                semaphore.acquire();

                // 运行到这里说明获取到了许可，可以去买票了
                log.info(">>>>>>>>>>用户"+ user + "获取信号量成功,进入窗口，余票："+ count +"准备买票..."+"<<<<<<<<<<");
                if (count > 0 ) {
                    count = count-1;
                    Thread.sleep((long)Math.random()*10000); // 模拟买票时间
                    log.info(">>>>>>>>>>用户"+ user + "买票完成，余票："+ count +"准备离开....<<<<<<<<<<");
                } else {
                    log.info(">>>>>>>>>>余票不足，用户"+ user + "没买到票...<<<<<<<<<<");
                }
                // 释放信号量许可证
                semaphore.release();
                log.info(">>>>>>>>>>用户"+ user + "已离开...<<<<<<<<<<");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void execute(){
        // 定义窗口个数
        final Semaphore s = new Semaphore(2);
        // 线程池
        ExecutorService threadPool = Executors.newCachedThreadPool();

        // 模拟20个用户
        for (int i = 0; i < 20; i++) {
            threadPool.execute(new MyTask(s,(i+1)));
        }
        // 关闭线程池
        threadPool.shutdown();
    }

    public static void main(String[] args) {
        SemaphoreDemo semaphoreDemo = new SemaphoreDemo();
        semaphoreDemo.execute();
    }
}
