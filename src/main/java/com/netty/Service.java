package com.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Service {

    int port;

    private void bind() {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup(2);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boss, worker);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024); // 连接数
            bootstrap.option(ChannelOption.TCP_NODELAY, true); // 不延迟，消息立即发送
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); // 长连接
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel){
                    ChannelPipeline p = socketChannel.pipeline();
                    ServerHandler serverHandler = new ServerHandler();
                    p.addLast(serverHandler);// 添加ServerHandler，用来处理Server端接收和处理消息的逻辑
                }
            });
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            if (channelFuture.isSuccess()) {
                System.out.println("启动Netty服务成功，端口号：" + this.port);
            }
            // 关闭连接
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            System.out.println("启动Netty服务异常，异常信息：" + e.getMessage());
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Service service =  new Service();
        service.port = 10086;
        service.bind();
    }
}
