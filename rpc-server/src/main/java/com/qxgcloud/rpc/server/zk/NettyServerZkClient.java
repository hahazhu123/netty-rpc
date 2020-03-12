package com.qxgcloud.rpc.server.zk;

import com.qxgcloud.rpc.server.zk.inet.InetAddressType;
import com.qxgcloud.rpc.server.zk.inet.InetDevice;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * Netty Server 注册到 ZK 的客户端工具
 */
public class NettyServerZkClient extends AbstractZkClient {
  private static final Logger logger = LoggerFactory.getLogger(NettyServerZkClient.class);

  private CuratorFramework client;

  public NettyServerZkClient() {
    super();
  }

  public NettyServerZkClient(String connAddress) {
    super(connAddress);
  }

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
    createNode(CreateMode.EPHEMERAL, path, new byte[0]);
    logger.info("register netty server:" + inetAddress.getHostAddress() + " to zookeeper successfully");
  }

  private void checkNettyServerNodeExists() {
    if (!pathExists(Constants.NETTY_SERVER)) {
      logger.info("creating node " + Constants.NETTY_SERVER);
      createNode(CreateMode.PERSISTENT, Constants.NETTY_SERVER, new byte[0]);
    }
  }



  public static void main(String[] args) {
    NettyServerZkClient zkClient = new NettyServerZkClient();
    zkClient.register(InetAddressType.VMnet8);
  }
}
