package com.qxgcloud.rpc.client.handler;

import com.alibaba.fastjson.JSONObject;
import com.qxgcloud.rpc.common.message.rpc.RpcMessageFuture;
import com.qxgcloud.rpc.common.message.rpc.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;


public class SimpleClientHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    // 将服务器返回结果设置到channel的attr中
//    ctx.channel().attr(AttributeKey.valueOf("response")).set(msg);
    // 关闭客户端的channel，使得客户端主逻辑能够继续运行
//    ctx.channel().close();
    RpcResponse response = JSONObject.parseObject(msg.toString(), RpcResponse.class);
    RpcMessageFuture.receive(response);

  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt instanceof IdleStateEvent) {
      IdleStateEvent event = (IdleStateEvent) evt;
      if (event.state() == IdleState.WRITER_IDLE) {
        ctx.channel().writeAndFlush("ping\r\n");
      }
    }
  }
}
