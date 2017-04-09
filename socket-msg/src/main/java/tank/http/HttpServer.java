package tank.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import tank.http.work.HttpServerHandler;
import tank.http.work.IHttpDispatcher;
import tank.http.work.UrlDispatcher;


/**
 * @Author: tank
 * @Email: kaixiong.tan@qq.com
 * @Date: 2017/4/5
 * @Version: 1.0
 * @Description:
 */
public class HttpServer {

    public static void main(String[] args) {
        start();
    }

    public static void start() {

        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workEventLoopGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();


        final IHttpDispatcher dispatcher = new UrlDispatcher();
        dispatcher.scanHttpAction("tank.http.handler");

        serverBootstrap.group(bossEventLoopGroup, workEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                        ch.pipeline().addLast(new HttpRequestDecoder());

                        ch.pipeline().addLast(new HttpObjectAggregator(10 * 1024 * 1024));

                        ch.pipeline().addLast(new HttpResponseEncoder());
                        //ch.pipeline().addLast(new ChunkedWriteHandler());
                        //ch.pipeline().addLast("deflater", new HttpContentCompressor()); //压缩
                        ch.pipeline().addLast(new HttpServerHandler(dispatcher));

                    }
                });

        try {
            ChannelFuture channelFuture = serverBootstrap.bind("127.0.0.1", 8080).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossEventLoopGroup.shutdownGracefully();
            workEventLoopGroup.shutdownGracefully();

        }


    }
}
