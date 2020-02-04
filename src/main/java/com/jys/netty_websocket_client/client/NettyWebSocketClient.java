package com.jys.netty_websocket_client.client;

import com.jys.netty_websocket_client.handler.ClientWebSocketHander;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.net.URI;
import java.net.URISyntaxException;

public class NettyWebSocketClient {

    private static final String HOST = "localhost";
    private static final int PORT = 7788;

    public void start() throws InterruptedException, URISyntaxException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpClientCodec()).addLast(new HttpObjectAggregator(65536)).addLast(new ClientWebSocketHander());
                        }
                    });

            URI websocketURI = new URI("ws://localhost:7788");
            HttpHeaders httpHeaders = new DefaultHttpHeaders();
            //进行握手
            WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(websocketURI, WebSocketVersion.V13, (String)null, true,httpHeaders);
            System.out.println("connect");
            final Channel channel=b.connect(websocketURI.getHost(),websocketURI.getPort()).sync().channel();
            handshaker.handshake(channel);
            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}
