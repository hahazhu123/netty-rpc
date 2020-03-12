package com.qxgcloud.rpc.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.qxgcloud.rpc")
public class NettyRpcApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(NettyRpcApplication.class, args);
    context.start();
  }

}
