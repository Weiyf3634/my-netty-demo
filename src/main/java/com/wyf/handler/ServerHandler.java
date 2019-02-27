package com.wyf.handler;

import com.dict.CharsetDict;
import com.wyf.util.LogUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.UnsupportedEncodingException;

/**
 * 服务端handler
 *
 *@author: Weiyf
 *@Date: 2019-02-26 14:56
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            simpleRead(ctx,msg);
            // 有分隔符处理信息
//      Delimiterread(ctx, msg);
        } catch (Exception e) {
            LogUtil.errorMsg("系统异常》》》%s",e.getMessage());
        }

    }

    private void simpleRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        ByteBuf bt = (ByteBuf) msg;
        //创建一个和buf同长度的字节数组
        byte[] reqByte = new byte[bt.readableBytes()];
        //将buf中的数据读取到数组中
        bt.readBytes(reqByte);

        String reqStr = new String(reqByte, CharsetDict.UTF8);
        LogUtil.infoMsg("server 接收到客户端的请求:%s",reqStr);
        String respStr = new StringBuilder("服务器的响应==>")
                .append(reqStr)
                .append(" 接收成功").toString();

        //返回给客户端的响应
        ctx.writeAndFlush(Unpooled.copiedBuffer(respStr.getBytes()));
        //.addListener(ChannelFutureListener.CLOSE);
        // 有了写操作（writeAndFlush）下面就不用释放msg
//      ReferenceCountUtil.release(msg);
    }

    /**
     * 有分隔符的请求信息，包含转码
     *
     *@author: Weiyf
     *@Date: 2019-02-26 15:14
     */
    private void Delimiterread(ChannelHandlerContext ctx, Object msg) {
        // 如果把msg直接转成字符串，必须在服务中心添加 socketChannel.pipeline().addLast(new StringDecoder());
        String reqStr = (String)msg;
        System.err.println("server 接收到请求信息是："+reqStr);
        String respStr = new StringBuilder("来自服务器的响应").append(reqStr).append("$_").toString();

        // 返回给客户端响应                                                                                                                                                       和客户端链接中断即短连接，当信息返回给客户端后中断
        ctx.writeAndFlush(Unpooled.copiedBuffer(respStr.getBytes())).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        LogUtil.infoMsg("服务端数据读取完毕》》》");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        try{
            ctx.close();
        } catch (RuntimeException e){
            LogUtil.errorMsg("服务端数据读取异常====>%s",e.getMessage());
        }

    }
}
