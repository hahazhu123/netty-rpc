package com.qxgcloud.rpc.client.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;

public class CGlibProxy {
  public static void main(String[] args) {
    // 1.获得Enhancer
    Enhancer enhancer = new Enhancer();
    // 2.为Enhancer设置代理的class或interface
    enhancer.setInterfaces(new Class[]{Math.class});
    // 3.实现代理的具体逻辑
    enhancer.setCallback(new MethodInterceptor() {
      @Override
      public Object intercept(Object instance, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("执行了动态代理...");
        System.out.println("代理的方法为:" + method.getName());
        System.out.print("传入的参数为:");
        Arrays.asList(args).forEach(ele -> System.out.print(ele + " "));
        System.out.println();
        return methodProxy.invokeSuper(instance, args);
      }
    });

    // 4.enhancer.create()创建代理对象
    Math math = (Math) enhancer.create();
    System.out.println(math.add(1, 2));
  }
}

class Math {
  public Integer add( Integer a, Integer b) {
    return a + b;
  }
}