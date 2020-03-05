package com.qxgcloud.other.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class BIOServer {
  public static void main(String[] args) throws IOException {
    ServerSocket sever = new ServerSocket();
    sever.bind(new InetSocketAddress("127.0.0.1", 80));

    Socket client = sever.accept();
    InputStream is = client.getInputStream();
    int ch;
    while ((ch = is.read()) != -1) {
      System.out.print((char)ch);
    }

  }
}
