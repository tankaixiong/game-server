package tank.msg.common;

import tank.msg.network.IMsgDispatcher;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/10
 * @Version: 1.0
 * @Description:
 */
@Deprecated
public class SessionMsg {


    private ArrayBlockingQueue queue;

    private Future future;

    private Thread thread;

    private IMsgDispatcher dispatcher;

    private SocketSession session;

    public SocketSession getSession() {
        return session;
    }

    public void setSession(SocketSession session) {
        this.session = session;
    }

    public IMsgDispatcher getDispatcher() {
        return dispatcher;
    }

    public void setDispatcher(IMsgDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public ArrayBlockingQueue getQueue() {
        return queue;
    }

    public void setQueue(ArrayBlockingQueue queue) {
        this.queue = queue;
    }

    public Future getFuture() {
        return future;
    }

    public void setFuture(Future future) {
        this.future = future;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }
}
