package com.qxgcloud.rpc.server.service.impl;

import com.qxgcloud.rpc.common.core.RpcResponse;
import com.qxgcloud.rpc.common.message.user.User;
import com.qxgcloud.rpc.common.message.user.UserLoginInfo;
import com.qxgcloud.rpc.common.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Override
  public String login(UserLoginInfo loginInfo) {
    if (loginInfo.getPassword() != null && loginInfo.getUsername() != null) {
      return "success";
    } else {
      return "fail";
    }
  }

  @Override
  public Boolean saveUser(User user) {
    return true;
  }

  @Override
  public Boolean changeInfo(String name, Integer age) {
    System.out.println("invoke changeInfo");
    System.out.println("name:" + name + ", age:" + age);
    return true;
  }
}
