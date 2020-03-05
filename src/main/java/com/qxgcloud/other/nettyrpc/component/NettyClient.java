package com.qxgcloud.other.nettyrpc.component;

import com.qxgcloud.other.nettyrpc.handler.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class NettyClient {
  private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
  private final String host;
  private final int port;
  public NettyClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public void start() throws Exception {
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      b.group(group)
              .channel(NioSocketChannel.class)
              .remoteAddress(new InetSocketAddress(host, port))
              .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                  ch.pipeline().addLast(new NettyClientHandler());
                }
              });
      ChannelFuture channelFuture = b.connect().sync();
//      channelFuture.channel().closeFuture().sync();
      Channel channel = channelFuture.sync().channel();
      for (int i = 0; i < 10; i++) {
        String message = "您好" + i;
        logger.info(message);
        Thread.sleep(1000L);
      }

    } finally {
//      group.shutdownGracefully().sync();
    }
  }

  public static void main(String[] args) throws Exception {
    new NettyClient("192.168.101.1", 8081).start();
  }
}
