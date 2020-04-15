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
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class RpcClient {
  private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);
  private static EventLoopGroup group = new NioEventLoopGroup(2);
  private static ChannelFuture future = null;

  @Autowired
  private ServerDetector serverDetector;

  @PostConstruct
  public void init() {
    RpcServer server = serverDetector.getRandomServer();
    connectToServer(server);
    serverDetector.watch(new ServerWatcher(server));
  }

  class ServerWatcher implements CuratorWatcher {
    private RpcServer currentServer;
    public ServerWatcher(RpcServer currentServer) {
      this.currentServer = currentServer;
    }

    @Override
    public void process(WatchedEvent event) throws Exception {
      logger.info("detect servers list changes");
      logger.info("old server list is:" + serverDetector.getServers());
      serverDetector.findServers();
      logger.info("new server list is:" + serverDetector.getServers());
      // 当前结点已经不存在于server list中了，需要重新连接一台服务器
      if (!serverDetector.contains(currentServer)) {
        close();
        RpcServer server = serverDetector.getRandomServer();
        connectToServer(server);
      }
      // 继续监听
      serverDetector.watch(this);
    }
  }

  private void connectToServer(RpcServer server) {
    if (server == null) throw new RuntimeException("no server available");
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
      future = bootstrap.connect(server.getAddress(), server.getPort()).sync();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
    }
  }



  public static void close() {
    try {
      if (future != null)
        future.channel().close().sync();
      if (group != null)
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
