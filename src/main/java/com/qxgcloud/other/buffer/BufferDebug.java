package com.qxgcloud.other.buffer;

import io.netty.buffer.*;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.nio.charset.Charset;
import static io.netty.buffer.Unpooled.copiedBuffer;


public class BufferDebug {
  public static void main(String[] args) {
    ByteBufAllocator ALLOC = UnpooledByteBufAllocator.DEFAULT;
    ByteToMessageDecoder.Cumulator cumulator = ByteToMessageDecoder.MERGE_CUMULATOR;
    System.out.println("聚合之前:");
    ByteBuf cumulation = Unpooled.EMPTY_BUFFER;
    System.out.println("cumulation:" + cumulation.toString(Charset.forName("UTF-8")));
    ByteBuf byteBuf1 = copiedBuffer("hello".getBytes());
    System.out.println("byteBuf1:" + byteBuf1.toString(Charset.forName("UTF-8")));
    ByteBuf byteBuf2 = copiedBuffer("word".getBytes());
    System.out.println("byteBuf2:" + byteBuf2.toString(Charset.forName("UTF-8")));
    System.out.println("聚合之后:");
    cumulation = cumulator.cumulate(ALLOC, cumulation, byteBuf1);
    cumulation = cumulator.cumulate(ALLOC, cumulation, byteBuf2);
    System.out.println("cumulation:" + cumulation.toString(Charset.forName("UTF-8")));
  }
}
