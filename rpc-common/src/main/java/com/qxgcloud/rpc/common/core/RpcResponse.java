package com.qxgcloud.rpc.common.core;

public class RpcResponse {
  private Long rpcId;
  private Integer code; // 状态码
  private String errorMsg;  // 错误信息
  private String result;  // 正确结果，是一个json对象

  public RpcResponse() {
  }

  public RpcResponse(String result, Integer code) {
    this.result = result;
    this.code = code;
  }

  public RpcResponse(Integer code, String errorMsg) {
    this.code = code;
    this.errorMsg = errorMsg;
  }


  public Long getRpcId() {
    return rpcId;
  }

  public void setRpcId(Long rpcId) {
    this.rpcId = rpcId;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  @Override
  public String toString() {
    return "RpcResponse{" +
            "rpcId=" + rpcId +
            ", code=" + code +
            ", errorMsg='" + errorMsg + '\'' +
            ", result=" + result +
            '}';
  }
}
