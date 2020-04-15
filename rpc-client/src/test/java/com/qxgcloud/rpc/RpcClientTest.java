package com.qxgcloud.rpc;

import com.qxgcloud.rpc.client.RpcClientApplication;
import com.qxgcloud.rpc.common.annotation.RpcService;
import com.qxgcloud.rpc.message.user.User;
import com.qxgcloud.rpc.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = RpcClientApplication.class)
public class RpcClientTest {

    @RpcService
    UserService userService;

    @Test
    public void testRequest() {
        User user = new User();
        user.setName("admin");
        user.setPassword("admin");
        User admin = userService.login(user);
        System.out.println(admin);
    }
}
