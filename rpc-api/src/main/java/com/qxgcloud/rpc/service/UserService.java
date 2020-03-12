package com.qxgcloud.rpc.service;

import com.qxgcloud.rpc.message.user.User;

public interface UserService {
  Boolean register(User user);

  User login(User user);

  User getUser(String uid);
}
