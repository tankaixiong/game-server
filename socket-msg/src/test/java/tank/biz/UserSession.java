package tank.biz;

import tank.msg.common.ThreadPoolManager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/5/4
 * @Version: 1.0
 * @Description:
 */
public class UserSession {

    private ArrayBlockingQueue<Object> queue = new ArrayBlockingQueue<Object>(10240);

    final ReentrantLock lock = new ReentrantLock(true);

    private volatile boolean isRun = false;

    public void addMsg(Object data) {

        queue.offer(data);


        try {
            lock.lock();

            if (!isRun) {
                isRun = true;
                run();
            }

        } finally {
            lock.unlock();
        }

    }

    public void run() {

        ThreadPoolManager.getExecutor().submit(new Thread() {

            @Override
            public void run() {

                while (true) {//
                    try {


                        Object data = queue.poll(1500, TimeUnit.MILLISECONDS);

                        if (data != null) {

                            try {
                                //TODO:处理业务逻辑
                            } catch (Exception e) {

                            }


                        } else {


                            try {
                                lock.lock();

                                if (queue.isEmpty()) {

                                    isRun = false;

                                    break;
                                }

                            } finally {
                                lock.unlock();
                            }


                        }


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

    }


}
