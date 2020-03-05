package com.qxgcloud.rpc.server.handler;

import com.alibaba.fastjson.JSONObject;
import com.qxgcloud.rpc.common.message.rpc.RpcRequest;
import com.qxgcloud.rpc.common.message.rpc.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SimpleServerHandler extends ChannelInboundHandlerAdapter {
  private static final Logger logger = LoggerFactory.getLogger(SimpleServerHandler.class);
  // 由于为每个客户端的channel在构建pipeline的时候，都是通过new来创建新的handler, 因此变量是安全的
  // 参考文章： https://blog.csdn.net/supper10090/article/details/78431948
  private final int maxLossTime = 3; // 最大允许超时失败的次数
  private int lossTime = 0;

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
//    System.out.println("收到客户端(" + ctx.channel().remoteAddress() + ")消息:" + msg.toString());
//    ctx.channel().writeAndFlush("is ok\r\n");
    if ("ping".equals(msg)) {
      return;
    }

    System.out.println("收到客户端(" + ctx.channel().remoteAddress() + ")消息:" + msg.toString());
    RpcRequest request = JSONObject.parseObject(msg.toString(), RpcRequest.class);
    // TODO 执行逻辑
    RpcResponse response = new RpcResponse();
    response.setRpcId(request.getRpcId());
    response.setContent("is ok");
    ctx.channel().writeAndFlush(JSONObject.toJSONString(response));
    ctx.channel().writeAndFlush("\r\n");
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt instanceof IdleStateEvent) {
      IdleStateEvent event = (IdleStateEvent) evt;
      if (event.state() == IdleState.ALL_IDLE) {
        lossTime++;
        logger.error("client(" + ctx.channel().remoteAddress() + ") lost " + lossTime +" time");
        if (lossTime > maxLossTime) {
          logger.error("client(" + ctx.channel().remoteAddress() + ") is not brisk, close channel");
          ctx.channel().close();
        }
      }
    }
  }
}
