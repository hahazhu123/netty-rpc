package com.qxgcloud.rpc.server.component;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class ServiceAgency {

  public static Map<String, ServiceMethod> serviceMap = new HashMap<>();

}


