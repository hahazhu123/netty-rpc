package com.qxgcloud.rpc.common.message.rpc;

import java.util.concurrent.atomic.AtomicLong;

public class RpcRequest {
  private Long rpcId;
  private Object content;
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

  public Object getContent() {
    return content;
  }

  public void setContent(Object content) {
    this.content = content;
  }

}
