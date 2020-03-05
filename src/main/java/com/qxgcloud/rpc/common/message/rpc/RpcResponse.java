package com.qxgcloud.rpc.common.message.rpc;

public class RpcResponse {
  private Long rpcId;
  private Object content;

  public Long getRpcId() {
    return rpcId;
  }

  public void setRpcId(Long rpcId) {
    this.rpcId = rpcId;
  }

  public Object getContent() {
    return content;
  }

  public void setContent(Object content) {
    this.content = content;
  }
}
