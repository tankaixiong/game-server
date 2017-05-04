package tank.msg.common;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/2
 * @Version: 1.0
 * @Description:
 */
public class ThreadPoolManager {

    private static ExecutorService executor;

    static {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("Request Handler-%d").build();
        executor = Executors.newCachedThreadPool(factory);
    }

    public static ExecutorService getExecutor() {
        return executor;
    }

    public static void print() {
        ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) executor;
        System.out.println(
                String.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
                        poolExecutor.getPoolSize(),
                        poolExecutor.getCorePoolSize(),
                        poolExecutor.getActiveCount(),
                        poolExecutor.getCompletedTaskCount(),
                        poolExecutor.getTaskCount(),
                        poolExecutor.isShutdown(),
                        poolExecutor.isTerminated()));


    }
}
