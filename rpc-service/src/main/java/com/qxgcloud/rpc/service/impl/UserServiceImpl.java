package com.qxgcloud.rpc.service.impl;

import com.qxgcloud.rpc.message.user.User;
import com.qxgcloud.rpc.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserServiceImpl implements UserService {

  User admin = new User("111", "admin", 100,
          "admin", "admin@qxgcloud.com", "18888888888");

  @Override
  public Boolean register(User user) {
    if (!StringUtils.isEmpty(user.getName()) && !StringUtils.isEmpty(user.getPassword())) {
      return true;
    }
    return false;
  }

  @Override
  public User login(User user) {
    if (user.getName().equals(admin.getName()) && user.getPassword().equals(admin.getPassword())) {
      return admin;
    }
    throw new RuntimeException("login fail");
  }

  @Override
  public User getUser(String uid) {
    if (uid.equals(admin.getUid())) {
      return admin;
    }
    throw new RuntimeException("user not found");
  }
}
