package com.wyf.client;

import com.dict.CharsetDict;
import com.dict.PortDict;
import com.wyf.handler.ClientHandler;
import com.wyf.util.MarshallingCodeUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.*;

/**
 * 客户端发送请求
 *
 *@author: Weiyf
 *@Date: 2019-02-27 10:20
 */
public class ClientNetty {

    //请求服务器的地址
    private String ip;

    //服务器的端口
    private int port;

    public ClientNetty(String ip,int port){
        this.ip = ip;
        this.port = port;
    }

    private void start(String msg) throws InterruptedException, IOException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bs = new Bootstrap();

        bs.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // marshalling 序列化对象的解码
                        ch.pipeline().addLast(MarshallingCodeUtil.buildDecoder());
                        // marshalling 序列化对象的编码
//                        ch.pipeline().addLast(MarshallingCodeUtil.buildEncoder());

                        // 处理来自服务端的响应信息
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });
        ChannelFuture cf = bs.connect(ip,port).sync();
        cf.channel().writeAndFlush(Unpooled.copiedBuffer(msg.getBytes(CharsetDict.UTF8)));

        //关闭连接
        cf.channel().close().sync();
        // 等待直到连接中断
//        cf.channel().closeFuture().sync();

//      Thread.sleep(300);
//      cf.channel().writeAndFlush(Unpooled.copiedBuffer("我是客户端请求2$_---".getBytes(Constant.charset)));
//      Thread.sleep(300);
//      cf.channel().writeAndFlush(Unpooled.copiedBuffer("我是客户端请求3$_".getBytes(Constant.charset)));

//      Student student = new Student();
//      student.setId(3);
//      student.setName("张三");
//      cf.channel().writeAndFlush(student);

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        for(;;){
            ClientNetty clientNetty = new ClientNetty("127.0.0.1", PortDict.Port8080);
            System.out.println("连接已建立");
            InputStreamReader ins = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(ins);
            String msg = br.readLine();
            clientNetty.start(msg);
        }


    }

}
