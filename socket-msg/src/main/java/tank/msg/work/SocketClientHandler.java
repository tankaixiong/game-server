package tank.msg.work;

import com.google.protobuf.TextFormat;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tank.handler.RequestType;
import tank.msg.code.IParser;
import tank.msg.code.MsgEntity;
import tank.msg.code.custom.JsonParser;
import tank.msg.code.custom.ProtobufParser;
import tank.msg.protoc.Packet;

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
    IParser protobufParser = new ProtobufParser();

    private void jsonWrite(ChannelHandlerContext ctx, Object obj) {
        byte[] out = jsonParser.encode(new MsgEntity(RequestType.TEST.getId(), obj));
        ctx.writeAndFlush(out);
    }

    private void protobufWrite(ChannelHandlerContext ctx, Object obj) {
        byte[] out = protobufParser.encode(new MsgEntity(RequestType.LOGIN.getId(), obj));
        ctx.writeAndFlush(out);
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

//        new Thread() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 20; i++) {
//
//                    jsonWrite(ctx, new MyMsg(1123, "测试一下"));
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();

        //ctx.close();
        //ctx.channel().closeFuture().addListener(ChannelFutureListener.CLOSE);


        Packet.LoginRequest.Builder loginRequest = Packet.LoginRequest.newBuilder();
        loginRequest.setId(1);
        loginRequest.setName("root");
        protobufWrite(ctx, loginRequest);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {

//        MsgEntity<String> msgEntity = jsonParser.decode(msg);
//        logger.info("收到消息:{}", msgEntity);

        MsgEntity<byte[]> msgEntity = protobufParser.decode(msg);
        Packet.LoginResponse response = Packet.LoginResponse.parseFrom(msgEntity.getData());
        logger.info("\n{}", TextFormat.printToString(response));
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
