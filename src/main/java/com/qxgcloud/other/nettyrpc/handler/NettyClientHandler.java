package com.qxgcloud.other.nettyrpc.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
  private final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    logger.info("客户端启动成功");
//    String message = "Message To Server";
//    ctx.writeAndFlush(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
//    logger.info("客户端发送数据: " + message);
    ctx.fireChannelActive();
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
    logger.info("客户端收到数据: " + msg.toString(CharsetUtil.UTF_8));
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
