package com.qxgcloud.rpc.common.hotupdate;

import com.qxgcloud.rpc.common.utils.FilePath;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MyClassLoader extends ClassLoader {

  /** 要加载的 Java 类的 classpath 路径 */
  private String classpath;

  private Map<String, ClassInfo> clazzMap = new HashMap<>();

  private FilePath path = new FilePath();

  public MyClassLoader(String classpath) {
    // 指定父加载器
    super(ClassLoader.getSystemClassLoader());
    this.classpath = classpath;
  }

  @Override
  protected Class<?> findClass(String name) {
    byte[] data = this.loadClassData(name);
    return this.defineClass(name, data, 0, data.length);
  }

  private byte[] loadClassData(String name) {
    try {
      // 传进来是带包名的
      name = name.replace(".", path.slash.toString());
      FileInputStream inputStream = new FileInputStream(new File(path.resolve(classpath, name) + ".class"));
      // 定义字节数组输出流
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      int b = 0;
      while ((b = inputStream.read()) != -1) {
        baos.write(b);
      }
      inputStream.close();
      return baos.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public Class<?> load(String name) {
    Class<?> clazz;
    if (clazzMap.get(name) == null) {
      clazz = findClass(name);
      ClassInfo classInfo = new ClassInfo(clazz, getClassLastModified(name));
      clazzMap.put(name, classInfo);
    } else {
      ClassInfo classInfo = clazzMap.get(name);
      boolean modified = CheckClassModify(name, classInfo);
      if (modified) {
        clazz = findClass(name);
        clazzMap.put(name, new ClassInfo(clazz, getClassLastModified(name)));
      } else {
        clazz = classInfo.getClazz();
      }
    }
    return clazz;
  }

  private long getClassLastModified(String name) {
    String clazzPath = path.resolve(classpath, name.replace(".", "/") + ".class");
    File file = new File(clazzPath);
    if (!file.exists()) {
      try {
        throw new FileNotFoundException(clazzPath);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
    return file.lastModified();
  }

  private boolean CheckClassModify(String name, ClassInfo classInfo) {
    long lastModified = getClassLastModified(name);
    if (lastModified == classInfo.getLastModified()) {
      return true;
    } else {
      return false;
    }
  }

  public static void main(String[] args) throws Exception {
//    MyClassLoader myClassLoader = new MyClassLoader("D:/code repo/qxg/netty-rpc/target/classes");
//    Class clazz = myClassLoader.load("com.qxgcloud.other.test.Hello");
//    System.out.println(clazz);
//    // 注意：不能进行强转
//    Object instance = clazz.newInstance();
//    Method method = clazz.getDeclaredMethod("say", null);
//    method.invoke(instance, null);

    new Thread(new TestTask()).start();
  }
}

class ClassInfo {
  private Class<?> clazz;
  private long lastModified;

  public ClassInfo(Class<?> clazz, long lastModified) {
    this.clazz = clazz;
    this.lastModified = lastModified;
  }

  public Class<?> getClazz() {
    return clazz;
  }

  public void setClazz(Class<?> clazz) {
    this.clazz = clazz;
  }

  public long getLastModified() {
    return lastModified;
  }

  public void setLastModified(long lastModified) {
    this.lastModified = lastModified;
  }
}

class TestTask implements Runnable {
  @Override
  public void run() {
    while (true) {
      try {
        MyClassLoader myClassLoader = new MyClassLoader("D:/code repo/qxg/netty-rpc/target/classes");
        Class<?> clazz = myClassLoader.load("com.qxgcloud.other.test.Hello");
        Object instance = clazz.newInstance();
        Method method = clazz.getDeclaredMethod("say", null);
        method.invoke(instance, null);
        Thread.sleep(2000);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}