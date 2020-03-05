package com.qxgcloud.rpc.server.component;

import java.lang.reflect.Method;

public class ServiceMethod {
  private Object service;
  private Method method;

  public ServiceMethod(Object service, Method method) {
    this.service = service;
    this.method = method;
  }

  public Object getService() {
    return service;
  }

  public void setService(Object service) {
    this.service = service;
  }

  public Method getMethod() {
    return method;
  }

  public void setMethod(Method method) {
    this.method = method;
  }
}
