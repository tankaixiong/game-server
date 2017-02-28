package tank.msg.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/2/28
 * @Version: 1.0
 * @Description:
 */
public class SocketClientHandler extends SimpleChannelInboundHandler<byte[]> {

    private Logger logger= LoggerFactory.getLogger(SocketClientHandler.class);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        ctx.writeAndFlush("test".getBytes("UTF-8"));
        ctx.writeAndFlush("再来一次再来一次再来一次再来一次再来一次再来一次再来一次再来一次再来一次再来一次再来一次".getBytes("UTF-8"));
        ctx.writeAndFlush("测试中文".getBytes("UTF-8"));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {

        String strMsg=String.valueOf(msg);
        logger.info("收到消息:{}",msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }
}
