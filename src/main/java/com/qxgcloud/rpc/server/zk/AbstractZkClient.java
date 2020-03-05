package com.qxgcloud.rpc.server.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractZkClient {
  private static final Logger logger = LoggerFactory.getLogger(NettyServerZkClient.class);

  private CuratorFramework client;

  public AbstractZkClient() {
    client = ZkFactory.instance();
  }

  public AbstractZkClient(String connAddress) {
    client = ZkFactory.instance(connAddress);
  }

  //TODO 出错即不存在，使用了伪意，还有其他情况发生造成出错，例如内部网络出错等
  public boolean pathExists(String path) {
    try {
      client.getData().forPath(Constants.NETTY_SERVER);
      return true;
    } catch (Exception e) {
      logger.error(e.getMessage());
      return false;
    }
  }

  public void createNode(CreateMode mode, String path, byte[] data) {
    try {
      client.create().withMode(mode).forPath(path, data);
    } catch (Exception e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
  }
}
