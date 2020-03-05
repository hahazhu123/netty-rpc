package com.qxgcloud.other.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {

  // 将一个字符串数组的参数解析成指定类型的java类型的对象数组
  public static List<Object> parse(Class<?>[] parameterTypes, String parameters) {
    JSONArray parameterArray = JSONObject.parseArray(parameters);
    if (parameterTypes.length != parameterArray.size()) {
      throw new RuntimeException("请求参数个数和请求方法参数个数不一致： 需要"
              + parameters.length() + "个参数，实际收到" + parameterArray.size() + "个参数");
    }
    List<Object> parameterObjects = null;
    for (int i = 0; i < parameterArray.size(); i++) {
      if (parameterObjects == null) {
        parameterObjects = new ArrayList<>();
      }
      String parameterStr = parameterArray.getString(i);
      if (JSONObject.isValid(parameterStr)) {
        Object parameterObject = JSONObject.parseObject(parameterStr, parameterTypes[i]);
      } else {
        Class<?> parameterType = parameterTypes[i];

      }

//      parameterObjects.add(parameterObject);
    }
    return parameterObjects;
  }

  public static void main(String[] args) {
    String str = "{\"age\": true}";

    System.out.println(JSONObject.isValid(str));
    System.out.println(JSONObject.isValidObject(str));
    System.out.println(JSONObject.isValidArray(str));
  }
}
