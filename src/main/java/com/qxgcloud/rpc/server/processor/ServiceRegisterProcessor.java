package com.qxgcloud.rpc.server.processor;

import com.qxgcloud.rpc.NettyRpcApplication;
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
    // TODO 建议采用自定义注册来进行服务开发
    if (bean.getClass().isAnnotationPresent(Service.class)) {
      logger.info("find service bean: " + bean);
      // TODO 检验是否两个接口，如果是，则进行报错，并停止服务启动
      String interfaceName = bean.getClass().getInterfaces()[0].getName();
      Method[] methods = bean.getClass().getDeclaredMethods();
      for (Method method: methods) {
        String serviceKey = interfaceName + "." + method.getName();
        ServiceMethod serviceMethod = new ServiceMethod(bean, method);
        ServiceAgency.serviceMap.put(serviceKey, serviceMethod);
        logger.info("register service: " + serviceKey);
      }

    }
    return bean;
  }
}
