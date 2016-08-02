package org.nesty.core.server.acceptor;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.nesty.core.server.NestyOptionProvider;
import org.nesty.core.server.NestyOptions;
import org.nesty.core.server.NestyServer;

/**
 * nesty
 *
 * Author Michael on 03/03/2016.
 */
public abstract class AsyncAcceptor implements IOAcceptor {

    private final String address;
    private final int port;

    private final NestyServer nestyServer;


    private ChannelFuture serverSocket;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public AsyncAcceptor(NestyServer nestyServer, String address, int port) {
        this.address = address;
        this.port = port;
        this.nestyServer = nestyServer;
    }

    @Override
    public void eventLoop() {

        // accept threads indicated the threads only work on accept() syscall,
        // receive client socket then transfor to io thread pool. bigger pool size
        // conform in SHORT CONNECTION scene (alse HTTP)
        int acceptThreads = Math.max(2, ((int) (nestyServer.option(NestyOptions.IO_THREADS) * 0.3) & 0xFFFFFFFE));


        // io threads (worker threads) handle client socket's read(), write()
        // and "business logic flow". in http short connection protocol socket's
        // read(), write() only a few times.
        //
        // HttpAdapter 's "business logic flow" has params building, validation,
        // logging , so we increase this pool percent in MaxThreads
        int rwThreads = Math.max(4, ((int) (nestyServer.option(NestyOptions.IO_THREADS) * 0.7) & 0xFFFFFFFE));

        bossGroup = new NioEventLoopGroup(acceptThreads);
        workerGroup = new NioEventLoopGroup(rwThreads);

        // initial request router's work threads
        AsyncRequestHandler.newTaskPool(nestyServer.option(NestyOptions.WORKER_THREADS));
        AsyncRequestHandler.useURLResourceController(nestyServer.getRouteController());
        AsyncRequestHandler.useInterceptor(nestyServer.getInterceptor());

        ServerBootstrap socketServer = new ServerBootstrap();
        socketServer.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        protocolPipeline(ch.pipeline(), nestyServer);
                    }
                })
                .option(ChannelOption.SO_TIMEOUT, nestyServer.option(NestyOptions.TCP_TIMEOUT))
                .option(ChannelOption.SO_BACKLOG, nestyServer.option(NestyOptions.TCP_BACKLOG))
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.TCP_NODELAY, nestyServer.option(NestyOptions.TCP_NODELAY))
                //.option(ChannelOption.SO_LINGER, 0)           // close no wait left data to send
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        // Bind and start to accept incoming connections.
        try {
            serverSocket = socketServer.bind(address, port).sync();
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public void join() throws InterruptedException {
        // sync waitting until the server socket is closed.
        serverSocket.channel().closeFuture().sync();
    }

    @Override
    public void shutdown() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    protected abstract void protocolPipeline(ChannelPipeline pipeline, NestyOptionProvider options);
}
