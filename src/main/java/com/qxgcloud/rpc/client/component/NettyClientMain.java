package com.qxgcloud.rpc.client.component;

import com.qxgcloud.rpc.client.handler.SimpleClientHandler;
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
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class NettyClientMain {
  private static final Logger logger = LoggerFactory.getLogger(NettyClientMain.class);

  public static void main(String[] args) throws Exception {
    String host = "192.168.101.1";
    int port = 8081;
    EventLoopGroup group = new NioEventLoopGroup(2);
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
      ChannelFuture future = bootstrap.connect(host, port).sync();
      future.channel().writeAndFlush("hello world\r\n");

      future.channel().closeFuture().sync();//挂起，如果该channel被关闭了，就会继续执行
      // 异步接收消息结果
      Object response = future.channel().attr(AttributeKey.valueOf("response")).get();
      System.out.println("接收到服务器的消息:" + response);
    } finally {
      group.shutdownGracefully().sync();
    }

  }
}
