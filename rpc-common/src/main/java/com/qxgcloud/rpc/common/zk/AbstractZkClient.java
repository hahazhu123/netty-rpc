package com.qxgcloud.rpc.common.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class AbstractZkClient {
  private static final Logger logger = LoggerFactory.getLogger(NettyServerZkClient.class);

  private CuratorFramework client;

  public AbstractZkClient() {
    client = ZkFactory.getClient();
  }

  public AbstractZkClient(String connAddress) {
    client = ZkFactory.getClient(connAddress);
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

  public void addWatcher(CuratorWatcher watcher) {
    try {
      client.getChildren().usingWatcher(watcher).forPath(Constants.NETTY_SERVER);
    } catch (Exception e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
  }

  public List<String> getChildren(String path) {
    try {
      return client.getChildren().forPath(path);
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
