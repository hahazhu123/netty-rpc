package com.qxgcloud.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NettyRpcApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(NettyRpcApplication.class, args);
    context.start();
  }

}
