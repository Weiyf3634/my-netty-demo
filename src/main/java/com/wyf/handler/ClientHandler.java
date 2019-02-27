package com.wyf.handler;

import com.dict.CharsetDict;
import com.wyf.dto.Student;
import com.wyf.util.LogUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 读取服务端返回的相应信息
 *
 *@author: Weiyf
 *@Date: 2019-02-27 10:12
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try{
            ByteBuf bt = (ByteBuf) msg;
            byte[] respByte = new byte[bt.readableBytes()];
            bt.readBytes(respByte);
            String respStr = new String(respByte, CharsetDict.UTF8);
            LogUtil.infoMsg("client 收到响应 %s",respStr);

            // 直接转成对象
//          handlerObject(ctx, msg);

        }finally {
            //必须释放msg数据
            ReferenceCountUtil.release(msg);
        }
    }

    private void handlerObject(ChannelHandlerContext ctx, Object msg) {

        Student student = (Student)msg;
        System.err.println("server 获取信息："+student.getId()+student.getName());
    }


    // 数据读取完毕的处理
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.err.println("客户端读取数据完毕");
    }

    // 出现异常的处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println("client 读取数据出现异常");
        ctx.close();
    }


}
