package com.qxgcloud.rpc.common.message.rpc;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RpcMessageFuture {
  public static ConcurrentMap<Long, RpcMessageFuture> messageMap = new ConcurrentHashMap<>(64);
  private Object lock = new Object();
  private RpcRequest request;
  private RpcResponse response;

  public RpcMessageFuture(RpcRequest request) {
    messageMap.put(request.getRpcId(), this);
    this.request = request;
  }

  public static void receive(RpcResponse response) {
    RpcMessageFuture messageFuture = messageMap.get(response.getRpcId());
    Object lock = messageFuture.getLock();
    synchronized (lock) {
      messageFuture.setResponse(response);
      lock.notify();
    }
  }

  public RpcResponse get() {
    synchronized (lock) {
      try {
        System.out.println("before wait");
        lock.wait();
        System.out.println("after wait");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      messageMap.remove(request.getRpcId());
      return response;
    }
  }

  public RpcResponse getResponse() {
    return response;
  }

  public void setResponse(RpcResponse response) {
    this.response = response;
  }

  public Object getLock() {
    return lock;
  }
}
