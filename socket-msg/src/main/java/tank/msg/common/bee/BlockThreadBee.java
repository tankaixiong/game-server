package tank.msg.common.bee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author tank
 * @version :0.1
 * @email kaixiong.tan@qq.com
 * @date:2015年8月11日 下午3:35:39
 * @description:通用的bee模式
 */

public abstract class BlockThreadBee<T> {

    private Logger LOG = LoggerFactory.getLogger(BlockThreadBee.class);

    //private static  int QUEUE_THREAD_NUM = 1;//默认消耗队列的线程数目,大于1，线程之间抢锁不一定会效率高，除非执行逻辑耗时长

    private static volatile BlockThreadBee context;
    protected LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<T>();// 时间序列上的更新库操作语句
    private volatile boolean exit = false;
    private List<Future> threadFuture = new ArrayList<Future>();

    private ExecutorService executor;

    public BlockThreadBee(ExecutorService executor) {

        this.executor = executor;

        before();

        LOG.info("================{}启动ThreadBee============", this.getClass().getName());

        initThread();
    }

    /**
     * 执行任务
     */
    public void initThread() {

        Future future = executor.submit(new Bee(), "ThreadBee-bee-" + this.getClass().getName());
        threadFuture.add(future);

        try {
            Thread.sleep(100);//制程线程时间差
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public boolean isExit() {
        return exit;
    }

    private void setExit(boolean exit) {
        this.exit = exit;
    }

    /**
     * 释放资源，正常退出
     */
    public void shutdown() {
        setExit(true);

        for (Future future : threadFuture) {
            future.cancel(true);//如果false是无法中断的
        }

    }

    @Deprecated
    public void runEmpty() {

    }

    public abstract void before();
    public abstract void after();

    public abstract void runBee(T t);

    public void put(T t) {
        try {
            queue.put(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
            LOG.error("添加对象到队列异常：{}", e);
        }
    }

    private class Bee extends Thread {

        public void run() {

            while (!exit) {
//				put         添加一个元素                      如果队列满，则阻塞
//				take        移除并返回队列头部的元素     如果队列为空，则阻塞
                try {
                    T t = queue.take();
                    if (t != null) {
                        runBee(t);
                    } else {
                        runEmpty();
                    }
                } catch (Exception e) {
                    LOG.error("{}", e);
                }

            }

            after();
        }
    }

}
