package com.qxgcloud.other.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class BIOClient {

  private static int port = 80;

  public static void main(String[] args) throws IOException, InterruptedException {
    Socket socket = new Socket("127.0.0.1", port);
    OutputStream os = socket.getOutputStream();
    os.write("hello".getBytes());
    Thread.sleep(3000);
    os.write("world".getBytes());
//    InputStream is = socket.getInputStream();

//    byte[] buff = new byte[1024];
//    int len;
//    while ((len = is.read(buff)) != -1) {
//      System.out.println(new String(buff, 0, len));
//    }

  }
}
