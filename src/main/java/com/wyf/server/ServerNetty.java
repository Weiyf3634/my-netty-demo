package com.wyf.server;

import com.dict.PortDict;
import com.wyf.handler.ServerHandler;
import com.wyf.util.LogUtil;
import com.wyf.util.MarshallingCodeUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * 服务端
 *
 *@author: Weiyf
 *@Date: 2019-02-26 15:48
 */
public class ServerNetty {

    /** 端口号 **/
    private int port;

    public ServerNetty(int port){
        this.port = port;
    }

    /** netty服务端启动 **/
    public void start() throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //用来处理已经被接收的连接，一旦bossGroup接收到连接，就会把连接信息注册到workerGroup上
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {

            //nio服务启动类
            ServerBootstrap sbs = new ServerBootstrap();
            //配置nio服务参数
            sbs.group(bossGroup, workGroup)
                    // 说明一个新的Channel如何接收进来的连接
                    .channel(NioServerSocketChannel.class)
                    // tcp最大缓存链接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //保持连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //打印日志级别
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // marshalling 序列化对象的解码
//                            ch.pipeline().addLast(MarshallingCodeUtil.buildDecoder());
                            // marshalling 序列化对象的编码
                            ch.pipeline().addLast(MarshallingCodeUtil.buildEncoder());
                            // 网络超时时间
                            ch.pipeline().addLast(new ReadTimeoutHandler(5));
                            // 处理接收到的请求，这里相当于过滤器，可以配置多个
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    });

            LogUtil.infoMsg("server开启服务>>>");
            //绑定端口，开始接受链接
            ChannelFuture cf = sbs.bind(port).sync();

            // 开多个端口
            //          ChannelFuture cf2 = sbs.bind(3333).sync();
            //          cf2.channel().closeFuture().sync();

            // 等待服务端口的关闭；在这个例子中不会发生，但你可以优雅实现；关闭你的服务
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
         new ServerNetty(PortDict.Port8080).start();
    }

}
