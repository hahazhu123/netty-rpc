package com.qxgcloud.rpc.common.core.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qxgcloud.rpc.common.core.RpcRequest;
import com.qxgcloud.rpc.common.core.RpcResponse;
import com.qxgcloud.rpc.common.core.exception.RpcException;
import org.springframework.util.StringUtils;

import java.util.List;

public class RpcMessageResolver {
  public static <T> T parseResponse(RpcResponse response, Class<T> clazz) {
    if (response.getCode() == 200 && !StringUtils.isEmpty(response.getResult())) {
      return JSONObject.parseObject(response.getResult(), clazz);
    } else {
      throw new RpcException(response.getCode(), response.getErrorMsg());
    }
  }

  // 将一个字符串数组的参数解析成指定类型的java类型的对象数组
  public static Object[] parseRequestArgs(RpcRequest request, Class[] classes) {
    JSONArray parameterArray = JSONObject.parseArray(request.getArgs());
    if (classes.length != parameterArray.size()) {
      throw new RpcException(400, "请求参数个数和请求方法参数个数不一致： 需要"
              + classes.length + "个参数，实际收到" + parameterArray.size() + "个参数");
    }
    List<Object> parameterObjects = JSONObject.parseArray(request.getArgs(), classes);
    return parameterObjects.toArray();
  }

}
