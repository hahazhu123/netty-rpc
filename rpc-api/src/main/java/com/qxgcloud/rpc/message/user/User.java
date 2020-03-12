package com.qxgcloud.rpc.message.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
  private String uid;
  private String name;
  private Integer age;
  private String password;
  private String email;
  private String phone;
}
