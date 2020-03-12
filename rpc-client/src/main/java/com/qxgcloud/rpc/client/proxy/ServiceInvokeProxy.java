package com.qxgcloud.rpc.client.proxy;

import com.alibaba.fastjson.JSONObject;
import com.qxgcloud.rpc.client.component.RpcClient;
import com.qxgcloud.rpc.common.annotation.RpcService;
import com.qxgcloud.rpc.common.core.RpcRequest;
import com.qxgcloud.rpc.common.core.RpcResponse;
import com.qxgcloud.rpc.common.core.utils.RpcMessageResolver;
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
import java.util.HashMap;
import java.util.Map;

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
        enhancer.setCallback(getRpcInterceptor(field));
        Object proxy = enhancer.create();
        try {
          field.set(bean, proxy);
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
    return bean;
  }

  private MethodInterceptor getRpcInterceptor(Field field) {
    Map<Method, Class> methodClassMap = new HashMap<>();
    Method[] methods = field.getType().getDeclaredMethods();
    for (Method method: methods) {
      methodClassMap.put(method, field.getType());
    }
    return new MethodInterceptor() {
      @Override
      public Object intercept(Object instance, Method method, Object[] args, MethodProxy methodProxy) {
        RpcRequest request = new RpcRequest();
        request.setMethod(method.getName());
        request.setArgs(JSONObject.toJSONString(args));
        request.setClazz(methodClassMap.get(method).getName());
        RpcResponse response = RpcClient.send(request);
        // 将请求结果进行解析
        return RpcMessageResolver.parseResponse(response, method.getReturnType());
      }
    };
  }
}
