package com.qxgcloud.other.nettyrpc.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MyExceptionHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    if (ctx instanceof RuntimeException) {
      //TODO
    } else if (ctx instanceof IllegalAccessException) {
      // TODO
    }
    // TODO
  }
}
