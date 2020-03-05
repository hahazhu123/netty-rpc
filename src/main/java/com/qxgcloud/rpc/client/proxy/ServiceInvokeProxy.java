package com.qxgcloud.rpc.client.proxy;

import com.qxgcloud.rpc.common.annotation.RpcService;
import com.qxgcloud.rpc.common.message.rpc.RpcRequest;
import com.qxgcloud.rpc.server.component.NettyServerInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Component
public class ServiceInvokeProxy implements BeanPostProcessor {
  private final Logger logger = LoggerFactory.getLogger(ServiceInvokeProxy.class);

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    Field[] fields = bean.getClass().getDeclaredFields();
    for (Field field: fields) {
      if (field.isAnnotationPresent(RpcService.class)) {
        field.setAccessible(true);
        Enhancer enhancer = new Enhancer();
        // 设置动态代理的范围是接口, 因为加有RpcService注解的字段必须是个接口类，而不能是个实现类
        enhancer.setInterfaces(new Class[]{field.getType()});
        // 设置动态代理的逻辑
        enhancer.setCallback((Callback) getRpcInterceptor());
        Object proxy = enhancer.create();
        try {
          field.set(bean,proxy);
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
    return bean;
  }

  private Object getRpcInterceptor() {
    return new MethodInterceptor() {

      @Override
      public Object intercept(Object instance, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        RpcRequest request = new RpcRequest();
        return null;
      }
    };
  }
}
