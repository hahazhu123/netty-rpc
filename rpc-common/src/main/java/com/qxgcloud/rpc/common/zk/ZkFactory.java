package com.qxgcloud.rpc.common.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Qualifier
public class ZkFactory {

  public static String zkAddress;

  @Value("${zk.address}:${zk.port}")
  public void setZkAddress(String zkAddress) {
    ZkFactory.zkAddress = zkAddress;
  }

  public static CuratorFramework getClient() {
    return getClient(zkAddress);
  }

  public static CuratorFramework getClient(String connAddress) {
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    CuratorFramework client = CuratorFrameworkFactory.newClient(connAddress, retryPolicy);
    client.start();
    return client;
  }
}
