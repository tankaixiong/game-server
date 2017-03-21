package tank.msg.common;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/3/2
 * @Version: 1.0
 * @Description:
 */
public class Session extends AbstractSession {

    //public static final String KEY_QUEUE = "queue";
    //public static final String KEY_THREAD = "thread";
    private long id;
    private Channel channel;

    public Session(Channel channel) {
        this.channel = channel;

        //setAttr(KEY_QUEUE, new ConcurrentLinkedQueue<byte[]>());
        //setAttr(KEY_THREAD, new RequestThread(this));
    }

    @Override
    public void write(Object obj) {
        channel.writeAndFlush(obj);
    }

    @Override
    public void write(Object obj, final CallBack callBack) {

        channel.writeAndFlush(obj).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                callBack.done(Session.this);
            }
        });
    }

    public Channel getChannel() {
        return channel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
