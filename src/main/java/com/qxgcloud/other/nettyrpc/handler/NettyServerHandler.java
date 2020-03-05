package com.qxgcloud.other.nettyrpc.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

  private final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

  @Override
  public void channelActive(ChannelHandlerContext ctx)   {
    logger.info("客户端连接成功!" + ctx.channel().remoteAddress());
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) {
    logger.info("客户端断开连接!{}", ctx.channel().remoteAddress());
    ctx.channel().close();
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    ByteBuf in = (ByteBuf) msg;
    System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
    // TODO
    // 1. 将消息(字符串)转化为统一的自定义接收对象(Request)
    // 2. 执行本地逻辑调用, 返回结果
    // 3. 将结果转化为统一的自定义响应对象(Response)
    // 4. 将Response转化为消息发出
    ByteBuf out = Unpooled.copiedBuffer("{\"code\": 200, \"data\": \"hello\"}\n", CharsetUtil.UTF_8);
    ctx.writeAndFlush(out);

  }
}
