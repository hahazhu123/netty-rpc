package com.qxgcloud.other.nettyrpc.component;

import com.qxgcloud.other.nettyrpc.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class NettyServer implements ApplicationContextAware, InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
  private static final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
  private static final EventLoopGroup workerGroup = new NioEventLoopGroup(4);

  @Override
  public void afterPropertiesSet() {
//    logger.info("启动netty服务器...");
//    start();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//    logger.info("invoke setApplicationContext");
//    logger.info("加载所有服务列表(bean)...");
  }

  private void start() {

    new Thread(() -> {
      try {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,  workerGroup).
                channel(NioServerSocketChannel.class).
                handler(new NettyParentGroupHandler()).
                childOption(ChannelOption.TCP_NODELAY,true).
                childHandler(new ChannelInitializer<SocketChannel>() {
                  //创建NIOSocketChannel成功后，在进行初始化时，将它的ChannelHandler设置到ChannelPipeline中，用于处理网络IO事件
                  protected void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();

                    pipeline.addLast(new NettyServerHandler());
//                    pipeline.addLast(new OutBoundHandlerA());
//                    pipeline.addLast(new OutBoundHandlerB());
//                    pipeline.addLast(new OutBoundHandlerC());
                  }
                });
        String host = "192.168.101.1";
        int port = 8081;
        ChannelFuture future = bootstrap.bind(host, port).sync();
        logger.info("RPC 服务器启动成功, 监听端口:" + port);

        //等待服务端监听端口关闭
        future.channel().closeFuture().sync();
      } catch (Exception e) {
        e.printStackTrace();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
      }
    }).start();

  }
}
