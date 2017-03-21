package tank.msg.common.bee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tank.msg.code.custom.JsonParser;
import tank.msg.common.MsgObj;
import tank.msg.common.ThreadPoolManager;
import tank.msg.work.IMsgDispatcher;
import tank.msg.work.MsgDispatcher;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/13
 * @Version: 1.0
 * @Description:
 */
public class ThreadBeeGroup {

    private Logger LOG = LoggerFactory.getLogger(ThreadBeeGroup.class);

    public ArrayList<BlockThreadBee> beeList = new ArrayList<>();

    private IMsgDispatcher dispatcher;
    private ExecutorService executor;
    private int threadNum;

    private CountDownLatch countDownLatch;

    public static ThreadBeeGroup getDefaultGroupBee() {

        ThreadBeeGroup threadBeeGroup = new ThreadBeeGroup();

        threadBeeGroup.setDispatcher(new MsgDispatcher(new JsonParser()));

        threadBeeGroup.setExecutor(ThreadPoolManager.getExecutor());

        threadBeeGroup.setThreadNum(Runtime.getRuntime().availableProcessors() + 1);

        threadBeeGroup.startGroup();

        return threadBeeGroup;
    }

    public ThreadBeeGroup() {
    }

    public ThreadBeeGroup(IMsgDispatcher dispatcher, ExecutorService executor, int threadNum) {
        this.dispatcher = dispatcher;
        this.executor = executor;
        this.threadNum = threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public ThreadBeeGroup setDispatcher(IMsgDispatcher dispatcher) {
        this.dispatcher = dispatcher;
        return this;
    }

    public ThreadBeeGroup setExecutor(ExecutorService executor) {
        this.executor = executor;
        return this;
    }

    public void addMsgObject(MsgObj msgObj) {

        int index = (int) (msgObj.getSession().getId() % beeList.size());

        beeList.get(index).put(msgObj);

    }

    public void startGroup() {

        countDownLatch = new CountDownLatch(threadNum);

        for (int i = 0; i < threadNum; i++) {

            BlockThreadBee threadBee = new BlockThreadBee<MsgObj>(executor) {
                @Override
                public void before() {

                }

                @Override
                public void after() {
                    LOG.info("线程退出");
                    countDownLatch.countDown();
                }

                @Override
                public void runBee(MsgObj msgObj) {

                    dispatcher.handler(msgObj.getSession(), msgObj.getData());

                }
            };

            beeList.add(threadBee);

        }

    }

    public void shutdown() {

        for (BlockThreadBee threadBee : beeList) {
            threadBee.shutdown();
        }
        try {
            countDownLatch.await();
            //countDownLatch.wait(120000);//等待全部关闭，超时为2分钟
        } catch (InterruptedException e) {
            e.printStackTrace();
            LOG.error("{}", e);
        }
    }

}
