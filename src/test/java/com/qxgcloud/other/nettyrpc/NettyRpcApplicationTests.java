package com.qxgcloud.other.nettyrpc;

import com.qxgcloud.rpc.NettyRpcApplication;
import com.qxgcloud.rpc.client.component.RpcClient;
import com.qxgcloud.rpc.common.annotation.RpcService;
import com.qxgcloud.rpc.common.message.rpc.RpcMessageFuture;
import com.qxgcloud.rpc.common.message.rpc.RpcRequest;
import com.qxgcloud.rpc.common.message.rpc.RpcResponse;
import com.qxgcloud.rpc.common.message.user.User;
import com.qxgcloud.rpc.common.service.UserService;
import com.qxgcloud.rpc.server.component.NettyServerInit;
import com.qxgcloud.rpc.server.processor.ServiceRegisterProcessor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = NettyRpcApplication.class)
class NettyRpcApplicationTests {

  @RpcService
  private UserService userService;

  @Autowired
  private NettyServerInit processor;

  @Test
  void testRpc() {
    for (int i = 1; i <= 10; i++) {
      RpcRequest request = new RpcRequest();
      request.setContent("hello");
      long start = System.nanoTime();
      RpcResponse response = RpcClient.send(request);
      long end = System.nanoTime();
      System.out.println(response.getContent());
      System.out.println("第" + i + "次消耗时间为: " + (end - start) / 1000+ " 微秒");
    }

    System.out.println(RpcMessageFuture.messageMap.size());
    RpcClient.close();
  }


  @Test
  void testRpc2() {
    User user = new User("Jack", "123");
    System.out.println(user);

//    String res = userService.saveUser(user);
//    System.out.println(res);
  }

}
