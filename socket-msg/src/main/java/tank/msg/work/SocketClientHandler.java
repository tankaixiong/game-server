package tank.msg.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tank.msg.code.IParser;
import tank.msg.code.MsgEntity;
import tank.msg.code.custom.JsonParser;
import tank.msg.protoc.MyMsg;

/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/2/28
 * @Version: 1.0
 * @Description:
 */
public class SocketClientHandler extends SimpleChannelInboundHandler<byte[]> {

    private Logger logger = LoggerFactory.getLogger(SocketClientHandler.class);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    IParser jsonParser = new JsonParser();

    private void write(ChannelHandlerContext ctx, Object obj) {

        byte[] out = jsonParser.encode(new MsgEntity(1, obj));
        ctx.writeAndFlush(out);
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {

                    write(ctx, new MyMsg(1123, "测试一下"));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        //ctx.close();
        //ctx.channel().closeFuture().addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {

        MsgEntity msgEntity = jsonParser.decode(msg);
        logger.info("收到消息:{}", msgEntity);
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
