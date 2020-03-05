package com.qxgcloud.rpc.server.component;

import com.qxgcloud.rpc.server.handler.SimpleServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class NettyServerInit implements ApplicationListener<ContextStartedEvent> {

  private final Logger logger = LoggerFactory.getLogger(NettyServerInit.class);

  @Value("${rpc.server.address}")
  private String address;

  @Value("${rpc.server.port}")
  private Integer port;

  @Override
  public void onApplicationEvent(ContextStartedEvent event) {
    // 初始化netty server
    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workerGroup = new NioEventLoopGroup(4);
    try {
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(bossGroup,  workerGroup)
              .channel(NioServerSocketChannel.class).
              option(ChannelOption.SO_BACKLOG, 128) // 处理不过来时，排队的连接数
              .childOption(ChannelOption.SO_KEEPALIVE,false) // 禁用TCP/IP 底层心跳机制，消耗性能，默认为false
              .childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel channel) {
                  ChannelPipeline pipeline = channel.pipeline();
                  pipeline.addLast(new DelimiterBasedFrameDecoder(65535, Delimiters.lineDelimiter()));
                  pipeline.addLast(new StringDecoder());
                  pipeline.addLast(new IdleStateHandler(0, 0, 5, TimeUnit.SECONDS));
                  pipeline.addLast(new SimpleServerHandler());
                  pipeline.addLast(new StringEncoder());
                }
              });

      ChannelFuture future = bootstrap.bind(address, port).sync();
      //将当前机器注册到zookeeper中
//      NettyServerZkClient zkClient = new NettyServerZkClient();
//      zkClient.register(InetAddressType.VMnet8);

      logger.info("NettyRpcServerInit is starting...");
      future.channel().closeFuture().sync();
    } catch (Exception e) {
      e.printStackTrace();
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }


}
