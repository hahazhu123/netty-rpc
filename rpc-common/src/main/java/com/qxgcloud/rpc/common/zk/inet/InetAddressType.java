package com.qxgcloud.rpc.common.zk.inet;

public enum InetAddressType {
  LOCAL("local"),
  VMnet8("VMnet8");

  private String type;

  InetAddressType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
