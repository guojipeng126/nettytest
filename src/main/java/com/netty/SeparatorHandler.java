package com.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

public class SeparatorHandler extends ChannelInitializer<SocketChannel> {

    ByteBuf buf = Unpooled.copiedBuffer("__".getBytes());

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
    }
}
