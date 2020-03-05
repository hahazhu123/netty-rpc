package com.qxgcloud.other.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 普通的NIOServer代码
 */
public class NIOServer {

  private static Selector selector = null;
  private static ByteBuffer readBuffer = ByteBuffer.allocate(10);

  public static void main(String[] args) throws IOException, InterruptedException {
    InetSocketAddress address = new InetSocketAddress(80);
    ServerSocketChannel server = ServerSocketChannel.open();

    server.bind(address);
    server.configureBlocking(false);

    selector = Selector.open();
    server.register(selector, SelectionKey.OP_ACCEPT);

    System.out.println("start server...");

    while(selector.select() > 0){
      Set<SelectionKey> keys = selector.selectedKeys();
      Iterator<SelectionKey> iterator = keys.iterator();
      while(iterator.hasNext()){
        SelectionKey key = iterator.next();
        HandlerSelectionKey(key);
        iterator.remove();
      }
    }
  }

  public static void HandlerSelectionKey(SelectionKey key) throws IOException, InterruptedException {
    if(key.isAcceptable()){
      //获取服务端channel,因为服务端channel注册了OP_ACCEPT
      //Returns the channel for which this key was created.
      ServerSocketChannel server = (ServerSocketChannel) key.channel();
      //获取客户端channel
      SocketChannel client = server.accept();
      //客户端channel也设置成非阻塞式的
      client.configureBlocking(false);
      // 禁止使用nagle算法
      client.socket().setTcpNoDelay(true);

      //将这个key再注册到选择器上，注册标记是可读
      client.register(selector, SelectionKey.OP_READ);
    }else if(key.isReadable()){
      //获取客户端channel，因为客户端channel注册了OP_READ
      //Returns the channel for which this key was created.
      SocketChannel client = (SocketChannel) key.channel();
      //将接收的数据打印到控制台上
      //将OutputStream(System.out)封装在一个通道中，将读取的数据写入与System.out连接的这个输出通道中
      WritableByteChannel out = Channels.newChannel(System.out);
      while(client.read(readBuffer) > 0){
        readBuffer.flip();
        //标记以下，因为要写两次，一次写到控制台，一次写到文件中
        out.write(readBuffer);
        readBuffer.clear();
      }
      //将这个key再注册到选择器上，注册标记是可写
      //读完了之后，想写回客户端，就像选择器注册
      client.register(selector, SelectionKey.OP_WRITE);
    }else if(key.isWritable()){
      SocketChannel client = (SocketChannel) key.channel();
      client.write(ByteBuffer.wrap("服务器已成功收到您的消息".getBytes()));
      client.close();
    }
  }
}
