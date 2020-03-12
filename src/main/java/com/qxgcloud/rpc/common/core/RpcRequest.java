package com.qxgcloud.rpc.common.core;

import java.util.concurrent.atomic.AtomicLong;

public class RpcRequest {
  private Long rpcId;
  private String clazz; //请求类
  private String method;  //请求方法
  private String args;  //请求参数,是一个json数组

  private static AtomicLong idGenerator = new AtomicLong(1);

  public RpcRequest() {
    rpcId = generateId();
  }

  private Long generateId() {
    return idGenerator.getAndIncrement();
  }

  public void setRpcId(Long rpcId) {
    this.rpcId = rpcId;
  }

  public Long getRpcId() {
    return rpcId;
  }

  public String getClazz() {
    return clazz;
  }

  public void setClazz(String clazz) {
    this.clazz = clazz;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getArgs() {
    return args;
  }

  public void setArgs(String args) {
    this.args = args;
  }
}
