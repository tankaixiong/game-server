package tank.msg;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tank.msg.code.delimiter.MsgDecoder;
import tank.msg.code.delimiter.MsgEncoder;
import tank.msg.common.Constant;
import tank.msg.work.SocketClientHandler;


/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/2/27
 * @Version: 1.0
 * @Description:
 */
public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 1789;
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();


        try {
            Bootstrap bootStrap = new Bootstrap();
            bootStrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            ByteBuf byteBuf = Unpooled.copiedBuffer(Constant.DELIMITER);

                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, byteBuf));
                            ch.pipeline().addLast(new MsgDecoder());
                            ch.pipeline().addLast(new MsgEncoder());

                            ch.pipeline().addLast(new SocketClientHandler());

                        }
                    });

            logger.info("连接服务器:{},{}", ip, port);
            ChannelFuture channelFuture = bootStrap.connect(ip, port).sync();

            channelFuture.channel().closeFuture().sync();


        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }


    }
}
