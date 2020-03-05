package com.qxgcloud.other.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ReflectionTest {
  public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {


  }

  public String hello(String name, Integer age) {
    return name + "," + age;
  }

}
