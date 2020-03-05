package com.qxgcloud.rpc.server.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZkFactory {

  private static String DEFAULT_CONN_ADDRESS = "localhost:2181";

  private static CuratorFramework client;


  public static CuratorFramework instance() {
    return instance(DEFAULT_CONN_ADDRESS);
  }

  public static CuratorFramework instance(String connAddress) {
    if (client == null) {
      RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
      client = CuratorFrameworkFactory.newClient(connAddress, retryPolicy);
      client.start();
    }
    return client;
  }
}
