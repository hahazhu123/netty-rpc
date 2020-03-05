package com.qxgcloud.other.nettyrpc.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyParentGroupHandler extends ChannelInboundHandlerAdapter {
  @Override
  public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    System.out.println("channelRegistered");
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("channelActive");
  }

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    System.out.println("handlerAdded");
  }
}
