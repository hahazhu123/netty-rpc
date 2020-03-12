package com.qxgcloud.rpc.common.core.exception;

public class RpcException extends RuntimeException {
  private Integer code;
  private String msg;

  public RpcException(Integer code, String msg) {
    super();
    this.code = code;
    this.msg = msg;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
