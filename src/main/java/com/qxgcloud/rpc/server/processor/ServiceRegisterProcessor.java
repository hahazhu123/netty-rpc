package com.qxgcloud.rpc.server.processor;

import com.qxgcloud.rpc.NettyRpcApplication;
import com.qxgcloud.rpc.server.component.NettyServerInit;
import com.qxgcloud.rpc.server.component.ServiceAgency;
import com.qxgcloud.rpc.server.component.ServiceMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

/**
 * 注册所有服务到ServiceAgency中
 */
@Component
public class ServiceRegisterProcessor implements BeanPostProcessor {

  public final Logger logger = LoggerFactory.getLogger(NettyRpcApplication.class);

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (bean.getClass().isAnnotationPresent(Service.class)) {
      logger.info("find service bean: " + bean);
      String className = bean.getClass().getName();
      Method[] methods = bean.getClass().getDeclaredMethods();
      for (Method method: methods) {
        String serviceKey = className + "." + method.getName();
        ServiceMethod serviceMethod = new ServiceMethod(bean, method);
        ServiceAgency.serviceMap.put(serviceKey, serviceMethod);
        logger.info("register service: " + serviceKey);
      }

    }
    return bean;
  }
}
