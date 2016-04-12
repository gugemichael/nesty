package org.nesty.core.httpserver.impl.async;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.nesty.core.httpserver.HttpServerRouteProvider;

import java.util.concurrent.TimeUnit;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public class IOAcceptor {

    private final String address;
    private final int port;

    private final HttpServerRouteProvider httpServer;


    private ChannelFuture serverSocket;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public IOAcceptor(HttpServerRouteProvider httpServer, String address, int port) {
        this.address = address;
        this.port = port;
        this.httpServer = httpServer;
    }

    public void eventLoop() throws InterruptedException {

        // accept threads indicated the threads only work on accept() syscall,
        // receive client socket then transfor to io thread pool. bigger pool size
        // conform in SHORT CONNECTION scene (alse HTTP)
        int acceptThreads = Math.max(2, ((int) (httpServer.options().getIoThreads() * 0.3) & 0xFFFFFFFE));


        // io threads (worker threads) handle client socket's read(), write()
        // and "business logic flow". in http short connection protocol socket's
        // read(), write() only a few times.
        //
        // HttpAdapter 's "business logic flow" has params building, validation,
        // logging , so we increase this pool percent in MaxThreads
        int rwThreads = Math.max(4, ((int) (httpServer.options().getIoThreads() * 0.7) & 0xFFFFFFFE));

        bossGroup = new NioEventLoopGroup(acceptThreads);
        workerGroup = new NioEventLoopGroup(rwThreads);

        // initial request router's work threads
        AsyncRequestRouter.newTaskPool(httpServer.options().getHandlerThreads());
        AsyncRequestRouter.useURLResourceController(httpServer.getRouteController());
        AsyncRequestRouter.useInterceptor(httpServer.getInterceptor());

        ServerBootstrap socketServer = new ServerBootstrap();
        socketServer.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast("nesty-timer", new ReadTimeoutHandler(httpServer.options().getHandlerTimeout(), TimeUnit.MILLISECONDS))
                                .addLast("nesty-http-decoder", new HttpRequestDecoder())
                                .addLast("nesty-http-aggregator", new HttpObjectAggregator(httpServer.options().getMaxPacketSize()))
                                .addLast("nesty-request-poster", AsyncRequestRouter.build())
                                .addLast("nesty-http-encoder", new HttpResponseEncoder());
                    }
                })
                .option(ChannelOption.SO_TIMEOUT, httpServer.options().getHandlerTimeout())
                .option(ChannelOption.SO_BACKLOG, httpServer.options().getMaxConnections())
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.TCP_NODELAY, true)
                //.option(ChannelOption.SO_LINGER, 0)           // close no wait left data to send
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        // Bind and start to accept incoming connections.
        serverSocket = socketServer.bind(address, port).sync();
    }

    public void join() throws InterruptedException {
        // sync waitting until the server socket is closed.
        serverSocket.channel().closeFuture().sync();
    }

    public void shutdown() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
