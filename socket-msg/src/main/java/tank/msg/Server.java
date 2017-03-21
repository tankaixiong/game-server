package tank.msg;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tank.msg.code.delimiter.MsgDecoder;
import tank.msg.code.delimiter.MsgEncoder;
import tank.msg.common.Constant;
import tank.msg.common.ThreadPoolManager;
import tank.msg.common.bee.ThreadBeeGroup;
import tank.msg.work.RequestManager;
import tank.msg.work.SocketServerHandler;

import java.util.Scanner;


/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/2/27
 * @Version: 1.0
 * @Description:
 */
public class Server {
    private static Logger LOGGER = LoggerFactory.getLogger(Server.class);


    EventLoopGroup bossEventLoopGroup;
    EventLoopGroup workerEventLoopGroup;
    ThreadBeeGroup threadBeeGroup;
    int port = 1789;


    public static void main(String[] args) {

        RequestManager.load("tank.handler");//请求处理注册

        Server server = new Server();

        server.start();

        server.applicationStopRegister();

        server.consoleScanner();//控制台输入命令
    }

    public void applicationStopRegister() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.err.println("*** 将要关闭应用程序,因为JVM正在被关闭 ***");

                    stopServer();

                } catch (Exception e) {
                    LOGGER.error("关闭应用程序失败: ", e);
                }
            }
        }, "Application Shutdown Hook"));
    }

    public void start() {


        Thread thread = new Thread() {
            @Override
            public void run() {

                bossEventLoopGroup = new NioEventLoopGroup(2);
                workerEventLoopGroup = new NioEventLoopGroup();


                threadBeeGroup = ThreadBeeGroup.getDefaultGroupBee();
                try {
                    ServerBootstrap serverBootstrap = new ServerBootstrap();

                    serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup)
                            .channel(NioServerSocketChannel.class)
                            .option(ChannelOption.TCP_NODELAY, false)
                            .option(ChannelOption.SO_KEEPALIVE, true)
                            .handler(new LoggingHandler(LogLevel.INFO))
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ByteBuf delimiter = Unpooled.copiedBuffer(Constant.DELIMITER);

                                    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                                    ch.pipeline().addLast(new MsgDecoder());
                                    ch.pipeline().addLast(new MsgEncoder());

                                    ch.pipeline().addLast(new SocketServerHandler(threadBeeGroup));
                                }
                            });
                    LOGGER.info("启动服务器,监听端口:{}", port);
                    ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

                    channelFuture.channel().closeFuture().sync();


                } catch (InterruptedException e) {
                    LOGGER.error("{}", e);
                } finally {
                    stopServer();
                }


            }
        };
        thread.start();


    }

    private void consoleScanner() {
        Scanner scanner = new Scanner(System.in, "UTF-8");
        while (true) {
            if (scanner.hasNextLine()) {
                try {
                    String read = scanner.nextLine();
                    if ("quit".equals(read) || "exit".equals(read)) {
                        scanner.close();
                        System.exit(0);
                    }

                } catch (Exception e) {
                    LOGGER.error("{}", e);
                }
            }
        }
    }

    private void stopServer() {

        //注意关闭顺序
        bossEventLoopGroup.shutdownGracefully();
        workerEventLoopGroup.shutdownGracefully();

        threadBeeGroup.shutdown();

        ThreadPoolManager.getExecutor().shutdown();

        LOGGER.info("程序退出");
    }
}
