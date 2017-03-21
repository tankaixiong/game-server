package tank.msg.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tank.msg.common.MsgObj;
import tank.msg.common.Session;
import tank.msg.common.bee.ThreadBeeGroup;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/2/27
 * @Version: 1.0
 * @Description:
 */
public class SocketServerHandler extends SimpleChannelInboundHandler<byte[]> {

    private Logger logger = LoggerFactory.getLogger(SocketServerHandler.class);
    public static final AttributeKey KEY_SESSION = AttributeKey.newInstance("session");
    //public static final String KEY_THREAD = "thread";
    private ThreadBeeGroup beeGroup;
    private final AtomicLong sessionIdAtomic = new AtomicLong();

    public SocketServerHandler(ThreadBeeGroup beeGroup) {
        this.beeGroup = beeGroup;
    }

    public SocketServerHandler() {
        this.beeGroup = ThreadBeeGroup.getDefaultGroupBee();
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        logger.debug("channelRegistered");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        logger.debug("channelActive");
        //保存到Channel attr
        Session session = new Session(ctx.channel());
        session.setId(sessionIdAtomic.incrementAndGet());
        ctx.channel().attr(KEY_SESSION).setIfAbsent(session);

        new AtomicLong().getAndIncrement();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {


        final Session session = (Session) ctx.channel().attr(KEY_SESSION).get();

        beeGroup.addMsgObject(new MsgObj(session, msg));

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        logger.debug("channelReadComplete");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.debug("channelInactive");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        logger.debug("channelUnregistered");
    }
}
