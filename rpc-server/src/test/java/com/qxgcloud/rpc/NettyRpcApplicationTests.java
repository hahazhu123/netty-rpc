//package com.qxgcloud.rpc;
//
//import com.qxgcloud.rpc.client.component.RpcClient;
//import com.qxgcloud.rpc.common.annotation.RpcService;
//import com.qxgcloud.rpc.common.core.RpcMessageFuture;
//import com.qxgcloud.rpc.common.core.RpcRequest;
//import com.qxgcloud.rpc.common.core.RpcResponse;
//import com.qxgcloud.rpc.common.message.user.User;
//import com.qxgcloud.rpc.common.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = NettyRpcApplication.class)
//class NettyRpcApplicationTests {
//
//  @RpcService
//  private UserService userService;
//
//
//  @Test
//  void testRpc() {
//    for (int i = 1; i <= 10; i++) {
//      RpcRequest request = new RpcRequest();
//
//      long start = System.nanoTime();
//      RpcResponse response = RpcClient.send(request);
//      long end = System.nanoTime();
//      System.out.println(response);
//      System.out.println("第" + i + "次消耗时间为: " + (end - start) / 1000+ " 微秒");
//    }
//
//    System.out.println(RpcMessageFuture.messageMap.size());
//    RpcClient.close();
//  }
//
//
//  @Test
//  void testRpc2() {
//    User user = new User("Jack", "123");
//    System.out.println(user);
//    Boolean success = userService.saveUser(user);
//    System.out.println(success);
//
//    success = userService.changeInfo("Jack", 123);
//    System.out.println(success);
//
//    RpcClient.close();
//  }
//
//}
