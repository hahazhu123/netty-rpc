package com.qxgcloud.rpc.client.component;

import com.alibaba.fastjson.JSONObject;
import com.qxgcloud.rpc.client.handler.SimpleClientHandler;
import com.qxgcloud.rpc.common.core.RpcMessageFuture;
import com.qxgcloud.rpc.common.core.RpcRequest;
import com.qxgcloud.rpc.common.core.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class RpcClient {
  private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);
  static EventLoopGroup group = new NioEventLoopGroup(2);
  static ChannelFuture future = null;
  static {
    String host = "192.168.101.1";
    int port = 8081;
    try {
      Bootstrap bootstrap = new Bootstrap();
      bootstrap.group(group)
              .channel(NioSocketChannel.class)
              .option(ChannelOption.SO_KEEPALIVE,false)
              .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) {
                  ChannelPipeline pipeline = channel.pipeline();
                  pipeline.addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()));
                  pipeline.addLast(new StringDecoder());
                  pipeline.addLast(new IdleStateHandler(0, 3, 0, TimeUnit.SECONDS));
                  pipeline.addLast(new SimpleClientHandler());
                  pipeline.addLast(new StringEncoder());
                }
              });
      future = bootstrap.connect(host, port).sync();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
    }
  }

  public static void close() {
    try {
      future.channel().close().sync();
      group.shutdownGracefully().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static RpcResponse send(RpcRequest request) {
    future.channel().writeAndFlush(JSONObject.toJSONString(request));
    future.channel().writeAndFlush("\r\n");
    RpcMessageFuture messageFuture = new RpcMessageFuture(request);
    RpcResponse response = messageFuture.get();
    return response;
  }


}
