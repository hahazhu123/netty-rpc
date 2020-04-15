package com.qxgcloud.rpc.common.zk;


import com.qxgcloud.rpc.common.zk.inet.InetAddressType;
import com.qxgcloud.rpc.common.zk.inet.InetDevice;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.List;

/**
 * Netty Server 注册到 ZK 的客户端工具
 */
@Component
@DependsOn("zkFactory")
public class NettyServerZkClient extends AbstractZkClient {
  private static final Logger logger = LoggerFactory.getLogger(NettyServerZkClient.class);

  public NettyServerZkClient() {
    super();
  }

  public NettyServerZkClient(String connAddress) {
    super(connAddress);
  }

  @Deprecated
  public void register(InetAddressType inetAddressType) {
    checkNettyServerNodeExists();

    InetAddress inetAddress;
    if (inetAddressType == InetAddressType.VMnet8) {
      inetAddress = InetDevice.getInetAddressForVMNet8();
    } else if (inetAddressType == InetAddressType.LOCAL) {
      inetAddress = InetDevice.getInetAddressForLocal();
    } else {
      inetAddress = InetDevice.getInetAddressDefault();
    }
    if (inetAddress == null) {
      throw new RuntimeException("not found InetAddress for " + inetAddressType.getType());
    }
    String path = Constants.NETTY_SERVER + "/" + inetAddress.getHostAddress();
    // 短暂（ephemeral）：客户端和服务器端断开连接后，创建的节点自己删除
    createNode(CreateMode.EPHEMERAL, path, new byte[0]);
    logger.info("register netty server:" + inetAddress.getHostAddress() + " to zookeeper successfully");
  }

  public void register(String serverAddress, Integer port) {
    checkNettyServerNodeExists();
    String path = Constants.NETTY_SERVER + "/" + serverAddress + ":" + port;
    // 短暂（ephemeral）：客户端和服务器端断开连接后，创建的节点自己删除
    createNode(CreateMode.EPHEMERAL, path, new byte[0]);
    logger.info("register netty server:" + serverAddress + " to zookeeper successfully");
  }

  public List<String> getServers() {
    return getChildren(Constants.NETTY_SERVER);
  }

  private void checkNettyServerNodeExists() {
    if (!pathExists(Constants.NETTY_SERVER)) {
      logger.info("creating node " + Constants.NETTY_SERVER);
      createNode(CreateMode.PERSISTENT, Constants.NETTY_SERVER, new byte[0]);
    }
  }



  public static void main(String[] args) throws Exception {
    NettyServerZkClient zkClient = new NettyServerZkClient("192.168.101.1:2181");

  }
}
