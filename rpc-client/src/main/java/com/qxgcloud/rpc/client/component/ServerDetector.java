package com.qxgcloud.rpc.client.component;

import com.qxgcloud.rpc.common.zk.NettyServerZkClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.curator.framework.api.CuratorWatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class ServerDetector {

  @Autowired
  private NettyServerZkClient zkClient;

  private List<RpcServer> servers;

  @PostConstruct
  private void init() {
    findServers();
  }

  public void findServers() {
    servers = new ArrayList<>();
    List<String> serversInfo = zkClient.getServers();
    for (String info: serversInfo) {
      String[] splits = info.split(":");
      servers.add(new RpcServer(splits[0], Integer.valueOf(splits[1])));
    }
  }

  public List<RpcServer> getServers() {
    return servers;
  }

  public RpcServer getRandomServer() {
    Random random = new Random();
    if (servers.size() == 0) return null;
    int i = random.nextInt(servers.size());
    return servers.get(i);
  }

  public void watch(CuratorWatcher watcher) {
    zkClient.addWatcher(watcher);
  }

  public boolean contains(RpcServer rpcServer) {
    return servers.contains(rpcServer);
  }
}

/**
 * 此处要注意，lombok已经为我们重写了equals方法：
 *     RpcServer server1 = new RpcServer("192.168.101.1", new Integer(8181));
 *     RpcServer server2 = new RpcServer("192.168.101.1", new Integer(8181));
 *     System.out.println(server1.equals(server2)); // true
 *
 * 如果不用lombok的话，返回的结果因为false。为了实现需求，要自己重写equals方法
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class RpcServer {
  private String address;
  private Integer port;

  @Override
  public String toString() {
    return address + ":" + port;
  }

}

