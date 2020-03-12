package com.qxgcloud.rpc.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RpcClientApplication {
  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(RpcClientApplication.class, args);
    context.start();
  }
}
