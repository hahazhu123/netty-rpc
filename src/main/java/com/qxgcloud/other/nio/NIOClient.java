package com.qxgcloud.other.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

public class NIOClient {

  private static int port = 82;

  public static void main(String[] args) throws IOException {
    InetSocketAddress address = new InetSocketAddress("127.0.0.1", port);
    SocketChannel client = SocketChannel.open(address);
    ByteBuffer buffer = ByteBuffer.allocate(10);
    client.write(ByteBuffer.wrap("hello,服务器,我是一个客户端......\r\n".getBytes("UTF-8")));
    client.write(ByteBuffer.wrap("再说一句话\n".getBytes("UTF-8")));
    //将OutputStream(System.out)封装在一个通道中，将读取的数据写入与System.out连接的这个输出通道中
    WritableByteChannel out = Channels.newChannel(System.out);
    while(client.read(buffer) != -1){
      buffer.flip();
      out.write(buffer);
      buffer.clear();
    }
  }
}
