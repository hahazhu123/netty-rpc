package com.qxgcloud.rpc.server.component;

import com.alibaba.fastjson.JSONObject;
import com.qxgcloud.rpc.common.core.RpcRequest;
import com.qxgcloud.rpc.common.core.RpcResponse;
import com.qxgcloud.rpc.common.core.utils.RpcMessageResolver;
import com.qxgcloud.rpc.server.handler.SimpleServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ServiceAgency {
  private static final Logger logger = LoggerFactory.getLogger(SimpleServerHandler.class);
  public static Map<String, ServiceMethod> serviceMap = new HashMap<>();

  public static RpcResponse process(RpcRequest rpcRequest) {
    String serviceKey = rpcRequest.getClazz() + "." + rpcRequest.getMethod();
    ServiceMethod serviceMethod = serviceMap.get(serviceKey);
    RpcResponse rpcResponse;
    if (serviceMethod == null) {
      rpcResponse = new RpcResponse(400,
              "not found with request service:" + rpcRequest.getClazz() + "." + rpcRequest.getMethod());
    } else {
      Object service = serviceMethod.getService();
      Method method = serviceMethod.getMethod();
      Object[] args = RpcMessageResolver.parseRequestArgs(rpcRequest, method.getParameterTypes());
      try {
        Object result = method.invoke(service, args);
        rpcResponse = new RpcResponse(JSONObject.toJSONString(result), 200);
      } catch (Exception e) {
        logger.error(e.getMessage());
        rpcResponse = new RpcResponse(400, e.getMessage());
      }
    }
    rpcResponse.setRpcId(rpcRequest.getRpcId());
    return rpcResponse;
  }
}


