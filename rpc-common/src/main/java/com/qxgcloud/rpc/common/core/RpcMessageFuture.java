package com.qxgcloud.rpc.common.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RpcMessageFuture {
  private static long DEFAULT_REQUEST_TIMEOUT = 3 * 1000; // 3 seconds
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

  /** 是否已经得到RPC的结果 **/
  private boolean received() {
    return response != null;
  }

  public RpcResponse get() {
    return get(DEFAULT_REQUEST_TIMEOUT);
  }

  public RpcResponse get(long timeout) {
    synchronized (lock) {
      try {
        lock.wait(timeout);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    if (!received()) {
      RpcResponse rpcResponse = new RpcResponse(400,
              "couldn't get any response from server in " + timeout + " ms, request timeout");
      rpcResponse.setRpcId(request.getRpcId());
      response = rpcResponse;
    }
    messageMap.remove(request.getRpcId());
    return response;
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
