package com.qxgcloud.rpc.common.service;

import com.qxgcloud.rpc.common.message.user.User;
import com.qxgcloud.rpc.common.message.user.UserLoginInfo;

public interface UserService {

  String login(UserLoginInfo loginInfo);

  String saveUser(User user);
}
