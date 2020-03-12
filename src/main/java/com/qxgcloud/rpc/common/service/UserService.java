package com.qxgcloud.rpc.common.service;

import com.qxgcloud.rpc.common.core.RpcResponse;
import com.qxgcloud.rpc.common.message.user.User;
import com.qxgcloud.rpc.common.message.user.UserLoginInfo;

public interface UserService {

  String login(UserLoginInfo loginInfo);

  Boolean saveUser(User user);

  Boolean changeInfo(String name, Integer age);
}
